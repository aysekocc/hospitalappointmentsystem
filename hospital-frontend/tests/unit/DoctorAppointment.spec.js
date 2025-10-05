import { mount } from "@vue/test-utils";
import DoctorAppointment from "@/views/DoctorAppointment.vue";
import axios from "axios";

jest.mock("axios");

describe("DoctorAppointmentPage.vue", () => {
  let wrapper;

  beforeEach(() => {
    jest.clearAllMocks();
    Storage.prototype.getItem = jest.fn(() => "mocked-token");
    wrapper = mount(DoctorAppointment, {
      global: {
        mocks: {
          $router: { push: jest.fn() },
        },
      },
    });
  });

  // -----------------------------
  it("başlangıçta karşılama ekranını gösterir", () => {
    expect(wrapper.vm.showWelcome).toBe(true);
    expect(wrapper.find(".welcome-screen").exists()).toBe(true);
  });

  // -----------------------------
  it("Randevuları Görüntüle butonuna tıklanınca fetchAppointments çağrılır ve karşılama ekranı kapanır", async () => {
    axios.get.mockResolvedValue({ data: [{ id: 1, username: "Hasta 1" }] });

    const button = wrapper.find(".btn-appointments");
    await button.trigger("click");

    expect(wrapper.vm.showWelcome).toBe(false);
    expect(axios.get).toHaveBeenCalledWith("/api/v1/appointment/my", expect.any(Object));
    expect(wrapper.vm.appointments.length).toBe(1);
  });

  // -----------------------------
  it("fetchAppointments sırasında token yoksa login sayfasına yönlendirir", async () => {
    Storage.prototype.getItem = jest.fn(() => null);
    wrapper = mount(DoctorAppointment, {
      global: { mocks: { $router: { push: jest.fn() } } },
    });

    await wrapper.vm.fetchAppointments();

    expect(wrapper.vm.$router.push).toHaveBeenCalledWith("/login");
  });

  // -----------------------------
  it("fetchAppointments hata alırsa errorMessage ayarlanır", async () => {
    axios.get.mockRejectedValue({ response: { status: 500 } });
    await wrapper.vm.fetchAppointments();
    expect(wrapper.vm.errorMessage).toBe("Randevular yüklenemedi.");
  });

  // -----------------------------
  it("openPrescriptionModal doğru şekilde modalı açar ve verileri ayarlar", async () => {
    const fakeAppointment = { id: 10, username: "Hasta X" };
    wrapper.vm.openPrescriptionModal(fakeAppointment);

    expect(wrapper.vm.showModal).toBe(true);
    expect(wrapper.vm.prescription.appointmentId).toBe(10);
  });

  // -----------------------------
  it("savePrescription başarılı olduğunda alert çağrılır ve modal kapanır", async () => {
    axios.post.mockResolvedValue({});
    window.alert = jest.fn();

    wrapper.vm.prescription = {
      medicineName: "Parol",
      diagnosis: "Ateş",
      appointmentId: 1,
    };

    await wrapper.vm.savePrescription();

    expect(axios.post).toHaveBeenCalledWith(
      "/api/v1/prescription/create",
      wrapper.vm.prescription,
      expect.any(Object)
    );
    expect(window.alert).toHaveBeenCalledWith("Reçete başarıyla kaydedildi!");
    expect(wrapper.vm.showModal).toBe(false);
  });

  // -----------------------------
  it("savePrescription hata aldığında alert hata mesajı gösterir", async () => {
    axios.post.mockRejectedValue(new Error("Sunucu hatası"));
    window.alert = jest.fn();

    await wrapper.vm.savePrescription();
    expect(window.alert).toHaveBeenCalledWith("Reçete kaydedilemedi.");
  });

  // -----------------------------
  it("goBackToWelcome çağrıldığında karşılama ekranına döner", async () => {
    wrapper.vm.showWelcome = false;
    wrapper.vm.appointments = [{ id: 1 }];

    wrapper.vm.goBackToWelcome();

    expect(wrapper.vm.showWelcome).toBe(true);
    expect(wrapper.vm.appointments).toEqual([]);
  });

  // -----------------------------
  it("formatDate geçerli tarihi doğru biçimlendirir", () => {
    const result = wrapper.vm.formatDate("2025-10-05T12:00:00Z");
    expect(result).toMatch(/\d{2}\.\d{2}\.\d{4}/); // TR formatında tarih bekleniyor
  });

  it("formatDate geçersiz tarih geldiğinde '-' döner", () => {
    expect(wrapper.vm.formatDate("geçersiz")).toBe("-");
  });
});
