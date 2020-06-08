package de.baumanngeorg.uvilsfrechner.datasource.dwd;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.Date;
import java.util.logging.Logger;

import de.baumanngeorg.uvilsfrechner.datasource.InternetResourceLoader;
import de.baumanngeorg.uvilsfrechner.service.StorageService;
import de.baumanngeorg.uvilsfrechner.datasource.dwd.model.DwdContainer;
import de.baumanngeorg.uvilsfrechner.datasource.dwd.model.DwdUviContent;
import de.baumanngeorg.uvilsfrechner.datasource.dwd.model.DwdUviModel;
import de.baumanngeorg.uvilsfrechner.view.main.CalculationFragment;
import lombok.Setter;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class UviRetrievingService {

    private static final Logger LOG = Logger.getLogger(UviRetrievingService.class.getName());

    @Setter
    private static UviRetrievingService instance = null;

    private final Gson gson;
    private final String url;
    private DwdContainer container;

    private UviRetrievingService() {
        gson = new Gson();
        url = "https://opendata.dwd.de/climate_environment/health/alerts/uvi.json";
        container = StorageService.getInstance().getStoredUviContainer();
        if (container == null) {
            LOG.info("Container is null! Getting new one");
            container = new DwdContainer();
        }
        LOG.severe(container.toString());
    }

    public static UviRetrievingService getInstance() {
        if (instance == null) {
            instance = new UviRetrievingService();
        }
        return instance;
    }

    public void setUvi(final CalculationFragment calculationFragment) {
        Date today = new Date();
        String stadt = StorageService.getInstance().getPreferredCity();
        DwdUviContent uviContent = container.getContentByCity(stadt);

        if (today.after(container.getNextUpdate())) {
            LOG.info("Load new data from DWD");

            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET, url, response -> {
                byte[] ptext = response.getBytes(ISO_8859_1);
                String value = new String(ptext, UTF_8);
                container.setModel(gson.fromJson(value, DwdUviModel.class));
                DwdUviContent uviContentNew = container.getContentByCity(stadt);
                StorageService.getInstance().setStoredUviContainer(container);

                calculationFragment.setUviSeekbar(getUviDependendOnDate(today, uviContentNew));
                calculationFragment.setUpdateString(container.getUpdateString(uviContentNew.getCity()));
            }, error -> {
            });
            stringRequest.setTag("UVI");

            InternetResourceLoader.getInstance().addRequest(stringRequest);
        } else {
            calculationFragment.setUviSeekbar(getUviDependendOnDate(today, uviContent));
            calculationFragment.setUpdateString(container.getUpdateString(uviContent.getCity()));
        }
    }

    private int getUviDependendOnDate(Date today, DwdUviContent uviContent) {
        int uvi;
        if (today.getDate() + today.getMonth() + today.getYear() == 5) {
            uvi = uviContent.getForecast().getToday();
        } else if (today.getDate() == 5) {
            uvi = uviContent.getForecast().getTomorrow();
        } else {
            uvi = uviContent.getForecast().getDayafter_to();
        }
        return uvi;
    }
}
