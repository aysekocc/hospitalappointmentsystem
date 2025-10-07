jest.spyOn(console, "error").mockImplementation(() => {}); // Konsol hatalarını bastır

import { mount, flushPromises } from "@vue/test-utils";
import AppointmentPage from "@/views/AppointmentPage.vue";
import axios from "axios";

jest.mock("axios");

describe("AppointmentPage.vue", () => {
  let wrapper;

  beforeEach(() => {
    axios.get.mockResolvedValue({ data: [] });
    Storage.prototype.getItem = jest.fn((key) => {
      if (key === "token") return "mocked-token";
      if (key === "userId") return "1";
      return null;
    });

    wrapper = mount(AppointmentPage, {
      global: {
        mocks: {
          $router: { push: jest.fn() },
        },
      },
    });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("Hoş geldiniz ekranını gösteriyor", () => {
    expect(wrapper.text()).toContain("Hoş Geldiniz!");
    expect(wrapper.find("button").text()).toContain("Randevu Oluştur");
  });

  it("Randevu Oluştur butonuna basınca form açılıyor", async () => {
    await wrapper.find("button").trigger("click");
    await flushPromises();

    expect(wrapper.vm.showForm).toBe(true);
    expect(wrapper.find("form").exists()).toBe(true);
    expect(wrapper.text()).toContain("Randevu Oluştur");
  });

  it("fetchDoctors doğru şekilde doktor listesini yüklüyor", async () => {
    const mockDoctors = [
      { id: 1, name: "Ahmet", surname: "Demir", specialty: "KARDIYOLOJI", title: "Dr." },
      { id: 2, name: "Zeynep", surname: "Yılmaz", specialty: "DAHILIYE", title: "Dr." },
    ];

    axios.get.mockResolvedValueOnce({ data: mockDoctors });

    await wrapper.vm.fetchDoctors();
    await flushPromises();

    expect(axios.get).toHaveBeenCalledWith("/api/v1/doctor/all", expect.any(Object));
    expect(wrapper.vm.doctors).toHaveLength(2);
    expect(wrapper.vm.doctors[0].name).toBe("Ahmet");
  });

  it("Randevu başarıyla oluşturulduğunda mesaj gösteriliyor", async () => {
    axios.post.mockResolvedValueOnce({});

    wrapper.vm.showForm = true;
    wrapper.vm.appointmentDate = "2025-10-10";
    wrapper.vm.appointmentTime = "09:00";
    wrapper.vm.appointment = {
      hospitalId: 1,
      specialty: "KARDIYOLOJI",
      doctor: 1,  // bu mutlaka set edilmeli
    };

    await wrapper.vm.createAppointment();
    await flushPromises();

    expect(axios.post).toHaveBeenCalledWith(
      "/api/v1/appointment/create",
      expect.objectContaining({
        doctor: 1,
        hospitalId: 1,
        specialty: "KARDIYOLOJI",
        startedDate: expect.any(String),
        endedDate: expect.any(String),
      }),
      expect.objectContaining({
        headers: expect.objectContaining({
          Authorization: "Bearer mocked-token",
        }),
      })
    );

    expect(wrapper.vm.successMessage).toBe("Randevu başarıyla oluşturuldu.");
    expect(wrapper.vm.errorMessage).toBe("");
  });


  it("API hatası dönerse hata mesajı gösteriliyor", async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { message: "Seçilen saat dolu." } },
    });

    await wrapper.setData({
      showForm: true,
      appointmentDate: "2025-10-11",
      appointmentTime: "10:30",
      appointment: {
        hospitalId: 2,
        specialty: "KBB",
        doctor: 2,
      },
    });

    await wrapper.find("form").trigger("submit.prevent");
    await flushPromises();

    expect(wrapper.vm.successMessage).toBe("");
    expect(wrapper.vm.errorMessage).toBe("Seçilen saat dolu.");
  });

  it("Tarih veya saat seçilmeden gönderilirse hata mesajı verir", async () => {
    await wrapper.setData({
      showForm: true,
      appointmentDate: "",
      appointmentTime: "",
      appointment: {
        doctor: 1,
        hospitalId: 1,
        specialty: "DAHILIYE",
      },
    });

    await wrapper.find("form").trigger("submit.prevent");
    await flushPromises();

    expect(wrapper.vm.errorMessage).toBe("Lütfen tarih ve saat seçin.");
    expect(wrapper.vm.successMessage).toBe("");
  });
});
