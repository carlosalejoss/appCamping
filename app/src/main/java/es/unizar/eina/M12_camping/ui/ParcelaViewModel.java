package es.unizar.eina.M12_camping.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaRepository;

public class ParcelaViewModel extends AndroidViewModel {

    private ParcelaRepository mRepository;

    private final LiveData<List<Parcela>> mAllParcelas;
    private final LiveData<List<Parcela>> mParcelasOrdNombre;
    private final LiveData<List<Parcela>> mParcelasOrdOcupantes;
    private final LiveData<List<Parcela>> mParcelasOrdPrecio;

    public ParcelaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllParcelas = mRepository.getAllParcelas();
        mParcelasOrdNombre = mRepository.getParcelasOrderedNombre();
        mParcelasOrdOcupantes = mRepository.getParcelasOrderedOcupantes();
        mParcelasOrdPrecio = mRepository.getParcelasOrderedPrecio();
    }

    LiveData<List<Parcela>> getAllParcelas() { return mAllParcelas; }

    LiveData<List<Parcela>> getParcelasOrderedNombre() {
        return mParcelasOrdNombre;
    }

    LiveData<List<Parcela>> getParcelasOrderedOcupantes() {
        return mParcelasOrdOcupantes;
    }

    LiveData<List<Parcela>> getParcelasOrderedPrecio() {
        return mParcelasOrdPrecio;
    }

    public void insert(Parcela parcela) { mRepository.insert(parcela); }

    public void update(Parcela parcela) { mRepository.update(parcela); }

    public void delete(Parcela parcela) { mRepository.delete(parcela); }
}
