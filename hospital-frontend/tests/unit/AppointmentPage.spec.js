jest.spyOn(console, "error").mockImplementation(() => {});

import { mount, flushPromises } from "@vue/test-utils";
import AppointmentPage from "@/views/AppointmentPage.vue";
import axios from "axios";

jest.mock("axios");

describe("AppointmentPage.vue", () => {
  let wrapper;

  beforeEach(() => {
    axios.get.mockResolvedValue({ data: [] });
    // localStorage mock
    Storage.prototype.getItem = jest.fn(() => "mocked-token");

    wrapper = mount(AppointmentPage, {
      global: {
        mocks: {
          $router: {
            push: jest.fn(),
          },
        },
      },
    });
  });

  it("Hoş geldiniz ekranını gösteriyor", () => {
    expect(wrapper.text()).toContain("Hoş Geldiniz!");
    expect(wrapper.find("button").text()).toContain("Randevu Oluştur");
  });

  it("Randevu Oluştur butonuna basınca form açılıyor", async () => {
    await wrapper.find("button").trigger("click");
    expect(wrapper.text()).toContain("Randevu Oluştur");
    expect(wrapper.find("form").exists()).toBe(true);
  });

  it("Doktor listesi fetchDoctors çağrısında yükleniyor", async () => {
    axios.get.mockResolvedValueOnce({
      data: [
        { id: 1, name: "Ahmet", surname: "Demir", specialty: "KARDIYOLOJI", title: "Dr." },
        { id: 2, name: "Zeynep", surname: "Yılmaz", specialty: "DAHILIYE", title: "Dr." },
      ],
    });

    await wrapper.vm.fetchDoctors();
    await flushPromises();

    expect(wrapper.vm.doctors.length).toBe(2);
    expect(axios.get).toHaveBeenCalledWith("/api/v1/doctor/all", expect.any(Object));
  });

  it("Randevu başarıyla oluşturulduğunda mesaj gösteriliyor", async () => {
    axios.post.mockResolvedValueOnce({});

    await wrapper.setData({ showForm: true });
    await wrapper.setData({
      appointment: {
        hospitalId: 1,
        specialty: "KARDIYOLOJI",
        doctor: 1,
        startedDate: "2025-10-05",
        endedDate: "2025-10-06",
      },
    });

    await wrapper.find("form").trigger("submit.prevent");
    await flushPromises();

    expect(axios.post).toHaveBeenCalledWith(
      "/api/v1/appointment/create",
      expect.any(Object),
      expect.any(Object)
    );
    expect(wrapper.vm.successMessage).toBe("Randevu başarıyla oluşturuldu.");
  });

  it("Randevu oluşturulamazsa hata mesajı gösteriliyor", async () => {
    axios.post.mockRejectedValueOnce(new Error("API hatası"));

    await wrapper.setData({ showForm: true });
    await wrapper.setData({
      appointment: {
        hospitalId: 2,
        specialty: "KBB",
        doctor: 2,
        startedDate: "2025-10-05",
        endedDate: "2025-10-06",
      },
    });

    await wrapper.find("form").trigger("submit.prevent");
    await flushPromises();

    expect(wrapper.vm.errorMessage).toBe("Randevu oluşturulamadı.");
  });
});
