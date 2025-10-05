import { shallowMount } from '@vue/test-utils';
import Login from '@/views/Login.vue';
import axios from 'axios';

jest.mock('axios'); // axios'u mockla

describe('Login.vue', () => {
  let wrapper;
  let pushMock;

  beforeEach(() => {
    pushMock = jest.fn();

    wrapper = shallowMount(Login, {
      global: {
        mocks: {
          $router: { push: pushMock },
          $route: { query: {} },
        },
      },
    });

    jest.spyOn(console, 'error').mockImplementation(() => {}); // console.error bastır
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('loginTitle computed property doğru çalışıyor', () => {
    wrapper.vm.roleParam = 'doctor';
    expect(wrapper.vm.loginTitle).toBe('Doktor Girişi');

    wrapper.vm.roleParam = 'user';
    expect(wrapper.vm.loginTitle).toBe('Hasta Girişi');

    wrapper.vm.roleParam = null;
    expect(wrapper.vm.loginTitle).toBe('Login');
  });

  it('başarılı doktor girişi router push yapıyor', async () => {
    wrapper.vm.roleParam = 'doctor';
    axios.post.mockResolvedValueOnce({
      data: { role: 'ROLE_DOCTOR', token: 'abc', userId: 1 },
    });

    wrapper.setData({ username: 'doc', password: '1234' });
    await wrapper.vm.login();

    expect(localStorage.getItem('token')).toBe('abc');
    expect(localStorage.getItem('role')).toBe('ROLE_DOCTOR');
    expect(localStorage.getItem('userId')).toBe('1');
    expect(pushMock).toHaveBeenCalledWith('/doctor-appointments');
  });

  it('başarılı kullanıcı girişi router push yapıyor', async () => {
    wrapper.vm.roleParam = 'user';
    axios.post.mockResolvedValueOnce({
      data: { role: 'ROLE_USER', token: 'xyz', userId: 2 },
    });

    wrapper.setData({ username: 'user', password: '1234' });
    await wrapper.vm.login();

    expect(localStorage.getItem('token')).toBe('xyz');
    expect(localStorage.getItem('role')).toBe('ROLE_USER');
    expect(localStorage.getItem('userId')).toBe('2');
    expect(pushMock).toHaveBeenCalledWith('/appointments');
  });

  it('role uyuşmazsa hata mesajı gösteriyor', async () => {
    wrapper.vm.roleParam = 'doctor';
    axios.post.mockResolvedValueOnce({
      data: { role: 'ROLE_USER', token: 'xyz', userId: 2 },
    });

    wrapper.setData({ username: 'user', password: '1234' });
    await wrapper.vm.login();

    expect(wrapper.vm.message).toBe('Giriş yetkiniz yok!');
    expect(pushMock).not.toHaveBeenCalled();
  });

  it('login başarısız olursa hata mesajı gösteriyor', async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { message: 'Kullanıcı bulunamadı' } },
    });

    wrapper.setData({ username: 'wrong', password: '1234' });
    await wrapper.vm.login();

    expect(wrapper.vm.message).toBe('Kullanıcı bulunamadı');
  });

  it('"Ana Sayfaya Dön" butonu router push yapıyor', async () => {
    wrapper.vm.roleParam = 'doctor';
    await wrapper.vm.goBack();
    expect(pushMock).toHaveBeenCalledWith('/');
  });
});
