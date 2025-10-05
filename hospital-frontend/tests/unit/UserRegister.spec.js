import { shallowMount } from '@vue/test-utils';
import UserRegister from '@/views/UserRegister.vue';
import axios from 'axios';

jest.mock('axios');

describe('UserRegister.vue', () => {
  let wrapper;
  let routerPushSpy;

  beforeEach(() => {
    routerPushSpy = jest.fn();

    wrapper = shallowMount(UserRegister, {
      global: {
        mocks: {
          $router: { push: routerPushSpy },
        },
      },
    });

    // console.error'i testlerde görmemek için mockluyoruz
    jest.spyOn(console, 'error').mockImplementation(() => {});
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('başarılı kayıt durumunda message ve registrationSuccess set ediliyor', async () => {
    axios.post.mockResolvedValueOnce({}); // başarı durumu

    wrapper.setData({
      identity: '12345678901',
      name: 'Ayşe',
      surname: 'Koç',
      age: 25,
      gender: 'FEMALE',
      username: 'ayse',
      password: '1234'
    });

    await wrapper.vm.registerUser();

    expect(axios.post).toHaveBeenCalledWith('/api/v1/auth/register/user', {
      identity: '12345678901',
      name: 'Ayşe',
      surname: 'Koç',
      age: 25,
      gender: 'FEMALE',
      username: 'ayse',
      password: '1234',
      role: 'ROLE_USER'
    });

    expect(wrapper.vm.message).toBe('Kayıt başarılı!');
    expect(wrapper.vm.registrationSuccess).toBe(true);
  });

  it('kayıt başarısız olduğunda hata mesajı set ediliyor', async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { message: 'Kullanıcı zaten var' } }
    });

    wrapper.setData({
      identity: '12345678901',
      name: 'Ayşe',
      surname: 'Koç',
      age: 25,
      gender: 'FEMALE',
      username: 'ayse',
      password: '1234'
    });

    await wrapper.vm.registerUser();

    expect(wrapper.vm.message).toBe('Kayıt başarısız! Kullanıcı zaten var');
    expect(wrapper.vm.registrationSuccess).toBe(false);
  });

  it('goToLogin çağrıldığında router.push ile yönlendirme yapıyor', () => {
    wrapper.vm.goToLogin();
    expect(routerPushSpy).toHaveBeenCalledWith('/login');
  });

  it('back button tıklaması router.push("/") çağırıyor', async () => {
    const backBtn = wrapper.find('.back-btn');
    await backBtn.trigger('click');
    expect(routerPushSpy).toHaveBeenCalledWith('/');
  });
});
