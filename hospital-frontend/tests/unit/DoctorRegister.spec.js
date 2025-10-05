jest.spyOn(console, "error").mockImplementation(() => {});
import { mount } from "@vue/test-utils";
import axios from "axios";
jest.mock('axios');
import DoctorRegister from "@/views/DoctorRegister.vue"; // <-- dosya adını seninkine göre güncelle


describe("DoctorRegister.vue", () => {
  let wrapper;
  const pushMock = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    wrapper = mount(DoctorRegister, {
      global: {
        mocks: {
          $router: { push: pushMock },
        },
      },
    });
  });

  it("Form alanları render ediliyor", () => {
    expect(wrapper.find("input[placeholder='Ad']").exists()).toBe(true);
    expect(wrapper.find("input[placeholder='Soyad']").exists()).toBe(true);
    expect(wrapper.find("select").exists()).toBe(true);
    expect(wrapper.find("button[type='submit']").exists()).toBe(true);
  });

  it("Kayıt başarılı olduğunda mesaj ve giriş butonu görünür", async () => {
    axios.post.mockResolvedValueOnce({});

    await wrapper.setData({
      name: "Ayşe",
      surname: "Koç",
      password: "123456",
      username: "aysekoc",
      specialty: "KARDIYOLOJI",
      title: "Dr.",
      gender: true,
      age: 30,
      hospitalId: 1,
    });

    await wrapper.find("form").trigger("submit.prevent");

    // axios çağrısı doğru yapıldı mı?
    expect(axios.post).toHaveBeenCalledWith("/api/v1/auth/register/doctor", expect.objectContaining({
      name: "Ayşe",
      surname: "Koç",
      username: "aysekoc",
    }));

    await wrapper.vm.$nextTick();

    expect(wrapper.text()).toContain("Kayıt başarılı!");
    expect(wrapper.vm.registrationSuccess).toBe(true);
    expect(wrapper.find(".login-btn").exists()).toBe(true);
  });

  it("Kayıt başarısız olursa hata mesajı gösteriliyor", async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { message: "Kullanıcı zaten var" }  },
    });

    await wrapper.setData({
      name: "Mehmet",
      surname: "Yılmaz",
      password: "123456",
      username: "mehmet",
      specialty: "DAHILIYE",
      title: "Dr.",
      gender: false,
      age: 40,
      hospitalId: 2,
    });

    await wrapper.find("form").trigger("submit.prevent");
    await wrapper.vm.$nextTick();

    expect(wrapper.text()).toContain("Kayıt başarısız!");
    expect(wrapper.find(".login-btn").exists()).toBe(false);
  });

  it("Giriş Yap butonu login sayfasına yönlendiriyor", async () => {
    // başarılı kayıt simülasyonu
    wrapper.setData({ registrationSuccess: true });
    await wrapper.vm.$nextTick(); // Vue DOM'unu günceller
    await wrapper.find(".login-btn").trigger("click");

    expect(pushMock).toHaveBeenCalledWith("/login");
  });
});
