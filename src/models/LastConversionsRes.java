package models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class LastConversionsRes {
    private String result;

    @SerializedName("time_next_update_utc")
    private String timeNextUpdateUtc;

    @SerializedName("base_code")
    private String baseCode;

    @SerializedName("conversion_rates")
    private Map<String, Double> conversionRates;

    @SerializedName("error-type")
    private String errorType;

    // Getters
    public String getResult() {
        return result;
    }

    public String getTimeNextUpdateUtc() {
        return timeNextUpdateUtc;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public String getErrorType() {
        return errorType;
    }
}
