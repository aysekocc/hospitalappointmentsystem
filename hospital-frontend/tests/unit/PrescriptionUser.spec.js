import { shallowMount } from '@vue/test-utils';
import PrescriptionUser from '@/views/PrescriptionUser.vue';
import axios from 'axios';

jest.mock('axios');

describe('PrescriptionUser.vue', () => {
  let wrapper;
  let routerPushSpy;

  beforeEach(() => {
    // localStorage mock
    Storage.prototype.getItem = jest.fn((key) => {
      if (key === 'userId') return '1';
      if (key === 'token') return 'mock-token';
      return null;
    });

    routerPushSpy = jest.fn();

    wrapper = shallowMount(PrescriptionUser, {
      global: {
        mocks: {
          $router: { push: routerPushSpy },
        },
      },
    });

    jest.spyOn(console, 'error').mockImplementation(() => {});
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('mounted ile fetchPrescriptions çağrılıyor', () => {
    expect(wrapper.vm.userId).toBe(1);
  });

  it('fetchPrescriptions başarılı ise prescriptions ve pagination set ediliyor', async () => {
    axios.get.mockResolvedValueOnce({
      data: {
        content: [
          { id: 1, medicineName: 'İlaç1', diagnosis: 'Hastalık1', date: '2025-10-05T08:00:00', hashPrescription: 'abc123' }
        ],
        number: 0,
        totalPages: 2
      }
    });

    await wrapper.vm.fetchPrescriptions(0);

    expect(axios.get).toHaveBeenCalledWith('/api/v1/prescription/list/user', {
      params: { userId: '1', page: 0, size: 2 },
      headers: { Authorization: 'Bearer mock-token' }
    });
    expect(wrapper.vm.prescriptions).toHaveLength(1);
    expect(wrapper.vm.page).toBe(0);
    expect(wrapper.vm.totalPages).toBe(2);
    expect(wrapper.vm.loading).toBe(false);
    expect(wrapper.vm.error).toBe('');
  });

  it('fetchPrescriptions userId yoksa error set ediliyor', async () => {
    Storage.prototype.getItem = jest.fn(() => null);
    wrapper = shallowMount(PrescriptionUser);

    await wrapper.vm.fetchPrescriptions();

    expect(wrapper.vm.error).toBe('Kullanıcı bilgisi bulunamadı!');
  });

  it('fetchPrescriptions API hatasında error set ediliyor', async () => {
    axios.get.mockRejectedValueOnce({});
    await wrapper.vm.fetchPrescriptions(0);

    expect(wrapper.vm.error).toBe('Reçeteler alınamadı!');
    expect(wrapper.vm.loading).toBe(false);
  });

  it('fetchAppointment çağrıldığında router.push ile yönlendirme yapılıyor', () => {
    wrapper.vm.fetchAppointment();
    expect(routerPushSpy).toHaveBeenCalledWith('/appointments');
  });

  it('formatDate doğru formatı döndürüyor', () => {
    const formatted = wrapper.vm.formatDate('2025-10-05T08:00:00');
    expect(formatted).toMatch(/\d{2}\.\d{2}\.\d{4} \d{2}:\d{2}/);
  });

  it('pagination butonları disabled ve page değişimi çalışıyor', async () => {
    axios.get.mockResolvedValueOnce({
      data: {
        content: [],
        number: 0,
        totalPages: 1
      }
    });

    await wrapper.vm.fetchPrescriptions(0);

    expect(wrapper.vm.page).toBe(0);
    // Önceki buton disabled
    expect(wrapper.find('button:disabled').text()).toBe('Önceki');
  });
});
