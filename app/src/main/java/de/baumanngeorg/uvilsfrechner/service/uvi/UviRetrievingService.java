package de.baumanngeorg.uvilsfrechner.service.uvi;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import de.baumanngeorg.uvilsfrechner.service.internet.InternetResourceLoader;
import de.baumanngeorg.uvilsfrechner.service.storage.StorageManager;
import de.baumanngeorg.uvilsfrechner.service.uvi.model.DwdContainer;
import de.baumanngeorg.uvilsfrechner.service.uvi.model.DwdUviContent;
import de.baumanngeorg.uvilsfrechner.service.uvi.model.DwdUviModel;
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
        container = StorageManager.getInstance().getStoredUviContainer();
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
        LocalDateTime today = LocalDateTime.now();
        String stadt = StorageManager.getInstance().getPreferredCity();
        DwdUviContent uviContent = container.getContentByCity(stadt);

        if (today.isAfter(container.getNextUpdate())) {
            LOG.info("Load new data from DWD");

            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET, url, response -> {
                byte[] ptext = response.getBytes(ISO_8859_1);
                String value = new String(ptext, UTF_8);
                container.model = gson.fromJson(value, DwdUviModel.class);
                DwdUviContent uviContentNew = container.getContentByCity(stadt);
                StorageManager.getInstance().setStoredUviContainer(container);

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

    private int getUviDependendOnDate(LocalDateTime today, DwdUviContent uviContent) {
        int uvi;
        if (today.getDayOfYear() == container.getForecastDay().getDayOfYear()) {
            uvi = uviContent.getForecast().getToday();
        } else if (today.getDayOfYear() == container.getForecastDay().plusDays(1).getDayOfYear()) {
            uvi = uviContent.getForecast().getTomorrow();
        } else {
            uvi = uviContent.getForecast().getDayafter_to();
        }
        return uvi;
    }
}
