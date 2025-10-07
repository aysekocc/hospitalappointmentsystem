import { mount, flushPromises } from "@vue/test-utils";
import AppointmentList from "@/views/AppointmentList.vue";
import axios from "axios";

jest.mock("axios");
window.confirm = jest.fn(() => true);
window.alert = jest.fn();

describe("AppointmentList.vue", () => {
  let wrapper;
  const mockRouterPush = jest.fn();

  async function createWrapper() {
    localStorage.setItem("token", "mocked-token");
    localStorage.setItem("userId", "1");

    axios.get
      .mockResolvedValueOnce({
        data: [
          {
            id: 1,
            startedDate: "2025-10-10T09:00:00",
            endedDate: "2025-10-10T09:30:00",
            userName: "Ali Yılmaz",
            hospitalName: "Erdemli Devlet H.",
            specialty: "KARDIYOLOJI",
            doctorName: "Ahmet Demir",
            title: "Dr.",
          },
        ],
      })
      .mockResolvedValueOnce({
        data: {
          medicineName: "Parol",
          diagnosis: "Grip",
        },
      });

    wrapper = mount(AppointmentList, {
      global: {
        mocks: {
          $router: { push: mockRouterPush },
        },
      },
    });

    await flushPromises();
    return wrapper;
  }

  beforeEach(async () => {
    jest.clearAllMocks();
    localStorage.clear();
    await createWrapper();
  });

  it("randevuları ve reçeteleri başarıyla yükler", () => {
    expect(wrapper.vm.appointments).toHaveLength(1);
    expect(wrapper.text()).toContain("Ali Yılmaz");
    expect(wrapper.text()).toContain("Parol");
    expect(wrapper.text()).toContain("Grip");
  });

  it("token veya userId yoksa login sayfasına yönlendirir", async () => {
    localStorage.clear();
    wrapper = mount(AppointmentList, {
      global: { mocks: { $router: { push: mockRouterPush } } },
    });
    await flushPromises();
    expect(mockRouterPush).toHaveBeenCalledWith("/login");
  });

  it("reçete alınamadığında prescriptions[appointmentId] null olur", async () => {
    axios.get.mockRejectedValueOnce(new Error("Network Error"));
    await wrapper.vm.fetchPrescription(99);
    expect(wrapper.vm.prescriptions[99]).toBeNull();
  });

  it("randevuyu başarıyla iptal eder", async () => {
    axios.delete.mockResolvedValueOnce({});
    await wrapper.vm.cancelAppointment(1);
    expect(wrapper.vm.appointments.length).toBe(0);
    expect(window.alert).toHaveBeenCalledWith("Randevu başarıyla iptal edildi!");
  });

  it("randevu iptali başarısız olursa hata alert gösterir", async () => {
    axios.delete.mockRejectedValueOnce(new Error("Sunucu hatası"));
    await wrapper.vm.cancelAppointment(1);
    expect(window.alert).toHaveBeenCalledWith("Randevu iptal edilemedi. Lütfen tekrar deneyin.");
  });

  it("403 hatasında yetkisiz erişim mesajı gösterir", async () => {
    axios.get.mockRejectedValueOnce({ response: { status: 403 } });
    wrapper = mount(AppointmentList, {
      global: { mocks: { $router: { push: mockRouterPush } } },
    });
    await flushPromises();
    expect(wrapper.vm.errorMessage).toBe(
      "Yetkisiz erişim! Lütfen giriş yapın veya farklı bir kullanıcı deneyin."
    );
  });

  it("formatDate geçerli tarih formatı döndürür", () => {
    const formatted = wrapper.vm.formatDate("2025-10-10T09:00:00");
    expect(formatted).toMatch(/10\.10\.2025/);
  });

  it("formatSpecialty alt çizgileri boşlukla değiştirir", () => {
    const result = wrapper.vm.formatSpecialty("BEYIN_CERRAHISI");
    expect(result).toBe("BEYIN CERRAHISI");
  });
});
