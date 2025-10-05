import { mount, flushPromises } from "@vue/test-utils";
import AppointmentPage from "@/views/AppointmentPage.vue";
import axios from "axios";

jest.mock("axios");

describe("AppointmentPage.vue", () => {
  let wrapper;
  const mockRouterPush = jest.fn();

  beforeEach(async () => {
    jest.clearAllMocks();
    localStorage.clear();

    axios.get.mockResolvedValue({ data: [
        { id: 1, name: "Ali", surname: "Yılmaz", title: "Dr.", specialty: "KARDIYOLOJI" },
        { id: 2, name: "Ayşe", surname: "Koç", title: "Dr.", specialty: "DAHILIYE" }
      ]});

    wrapper = mount(AppointmentPage, {
      global: {
        mocks: {
          $router: { push: mockRouterPush }
        }
      }
    });

    await flushPromises();
  });

  // === 1. Başlangıç ekranı testi ===
  it("ilk olarak karşılama ekranını gösterir", () => {
    expect(wrapper.find(".welcome-screen").exists()).toBe(true);
    expect(wrapper.find("form").exists()).toBe(false);
  });

  // === 2. Randevu oluştur butonuna basıldığında formun görünmesi ===
  it("Randevu oluştur butonuna basıldığında form görünür", async () => {
    await wrapper.find("button.btn-submit").trigger("click");
    expect(wrapper.find("form").exists()).toBe(true);
  });

  // === 3. Doktorların fetch edilmesi ===
  it("fetchDoctors çağrıldığında doktor listesini yükler", async () => {
    expect(axios.get).toHaveBeenCalledWith("/api/v1/doctor/all", expect.any(Object));
    expect(wrapper.vm.doctors.length).toBe(2);
  });

  // === 4. Branşa göre doktor filtreleme ===
  it("branş seçildiğinde sadece o branştaki doktorları gösterir", async () => {
    await wrapper.setData({
      appointment: { specialty: "KARDIYOLOJI", doctor: null }
    });
    const filtered = wrapper.vm.filteredDoctors;
    expect(filtered.length).toBe(1);
    expect(filtered[0].name).toBe("Ali");
  });

  // === 5. Token yoksa login sayfasına yönlendirir ===
  it("token yoksa createAppointment login sayfasına yönlendirir", async () => {
    await wrapper.setData({
      showForm: true,
      appointment: {
        startedDate: "2025-10-10",
        endedDate: "2025-10-11",
        doctor: 1,
        hospitalId: 123,
        specialty: "KARDIYOLOJI"
      }
    });
    await wrapper.vm.createAppointment();
    expect(mockRouterPush).toHaveBeenCalledWith("/login");
  });

  // === 6. Token varsa randevu oluşturma isteği ===
  it("token varsa randevu başarıyla oluşturur", async () => {
    localStorage.setItem("token", "mocked-token");
    axios.post.mockResolvedValueOnce({});

    await wrapper.setData({
      showForm: true,
      appointment: {
        startedDate: "2025-10-10",
        endedDate: "2025-10-11",
        doctor: 1,
        hospitalId: 123,
        specialty: "KARDIYOLOJI"
      }
    });

    await wrapper.vm.createAppointment();
    expect(axios.post).toHaveBeenCalledWith(
      "/api/v1/appointment/create",
      expect.any(Object),
      expect.objectContaining({
        headers: { Authorization: "Bearer mocked-token" }
      })
    );
    expect(wrapper.vm.successMessage).toBe("Randevu başarıyla oluşturuldu.");
  });

  // === 7. API hatasında hata mesajı gösterir ===
  it("randevu oluşturulamadığında hata mesajı gösterir", async () => {
    localStorage.setItem("token", "mocked-token");
    axios.post.mockRejectedValueOnce(new Error("Network Error"));

    await wrapper.setData({
      showForm: true,
      appointment: {
        startedDate: "2025-10-10",
        endedDate: "2025-10-11",
        doctor: 1,
        hospitalId: 123,
        specialty: "KARDIYOLOJI"
      }
    });

    await wrapper.vm.createAppointment();
    expect(wrapper.vm.errorMessage).toBe("Randevu oluşturulamadı.");
  });
});
