package models;

import java.util.List;

public record SupportedCodesRes(
        String result,
        List<List<String>> supported_codes
) {

}
