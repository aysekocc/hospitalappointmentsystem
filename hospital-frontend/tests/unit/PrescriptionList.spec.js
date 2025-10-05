import { shallowMount } from '@vue/test-utils';
import PrescriptionList from '@/views/PrescriptionList.vue';
import axios from 'axios';

jest.mock('axios');

describe('PrescriptionList.vue', () => {
  let wrapper;
  let confirmSpy;
  let alertSpy;

  beforeEach(() => {
    // localStorage mock
    Storage.prototype.getItem = jest.fn(() => 'mock-token');

    wrapper = shallowMount(PrescriptionList);

    // confirm ve alert mock
    confirmSpy = jest.spyOn(window, 'confirm').mockImplementation(() => true);
    alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});
    jest.spyOn(console, 'error').mockImplementation(() => {});
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('fetchPrescriptions çağrıldığında API çağrılıyor ve data set ediliyor', async () => {
    axios.get.mockResolvedValueOnce({
      data: { content: [{ id: 1, medicineName: 'İlaç1', diagnosis: 'Hastalık1' }], last: true }
    });

    wrapper.setData({ userId: 123 });
    await wrapper.vm.fetchPrescriptions();

    expect(axios.get).toHaveBeenCalledWith(
      '/api/v1/prescription/list/user?userId=123&page=0&size=5',
      expect.any(Object)
    );
    expect(wrapper.vm.prescriptions).toHaveLength(1);
    expect(wrapper.vm.hasNextPage).toBe(false);
  });

  it('fetchPrescriptions userId yoksa alert gösteriyor', async () => {
    wrapper.setData({ userId: '' });
    await wrapper.vm.fetchPrescriptions();
    expect(alertSpy).toHaveBeenCalledWith('Kullanıcı ID giriniz!');
  });

  it('searchByHash API çağrısı yapıyor ve hashPrescription set ediliyor', async () => {
    axios.get.mockResolvedValueOnce({
      data: { medicineName: 'İlaç1', diagnosis: 'Hastalık1' }
    });

    wrapper.setData({ hash: 'abc123' });
    await wrapper.vm.searchByHash();

    expect(axios.get).toHaveBeenCalledWith(
      '/api/v1/prescription/list/hash?hash=abc123',
      expect.any(Object)
    );
    expect(wrapper.vm.hashPrescription.medicineName).toBe('İlaç1');
  });

  it('searchByHash hash boşsa alert gösteriyor', async () => {
    wrapper.setData({ hash: '' });
    await wrapper.vm.searchByHash();
    expect(alertSpy).toHaveBeenCalledWith('Hash kodu giriniz!');
  });

  it('deletePrescription confirm false ise çağrılmıyor', async () => {
    confirmSpy.mockImplementationOnce(() => false);
    await wrapper.vm.deletePrescription(1);
    expect(axios.delete).not.toHaveBeenCalled();
  });

  it('deletePrescription başarılı ise fetchPrescriptions çağrılıyor', async () => {
    axios.delete.mockResolvedValueOnce({});
    wrapper.vm.fetchPrescriptions = jest.fn();

    await wrapper.vm.deletePrescription(1);

    expect(axios.delete).toHaveBeenCalledWith(
      '/api/v1/prescription/1',
      expect.any(Object)
    );
    expect(wrapper.vm.fetchPrescriptions).toHaveBeenCalled();
  });

  it('API hatalarında alert gösteriliyor', async () => {
    axios.get.mockRejectedValueOnce({});
    wrapper.setData({ userId: 123 });
    await wrapper.vm.fetchPrescriptions();
    expect(alertSpy).toHaveBeenCalledWith('Reçeteler yüklenemedi!');

    axios.get.mockRejectedValueOnce({});
    wrapper.setData({ hash: 'abc' });
    await wrapper.vm.searchByHash();
    expect(alertSpy).toHaveBeenCalledWith('Reçete bulunamadı!');

    axios.delete.mockRejectedValueOnce({});
    await wrapper.vm.deletePrescription(1);
    expect(alertSpy).toHaveBeenCalledWith('Silme işlemi başarısız!');
  });
});
