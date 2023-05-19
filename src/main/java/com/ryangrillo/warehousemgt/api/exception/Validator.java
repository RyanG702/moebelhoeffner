package com.ryangrillo.warehousemgt.api.exception;

import com.ryangrillo.warehousemgt.api.model.Tag;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static void validateHoldingPoints(int holdingPoints) {
        if(holdingPoints < 1 || holdingPoints > 9) {
            throw new DataInputException("holding points must be between 1 and 9");
        }
    }

    public static void validateTags(List<Tag> tags) {
        for (Tag tag: tags) {
            Pattern pattern = Pattern.compile("[a-z]+");
            Matcher matcher = pattern.matcher(tag.getName());
            if (!matcher.matches() ) {
                throw new DataInputException("Tags are required and must be lowercase letters");
            }
        }
    }

    public static void validateWarehouseId(String identifierCode) {
        Pattern pattern = Pattern.compile("\\d{3}");
        Matcher matcher = pattern.matcher(identifierCode);
        if (!matcher.matches() ) {
            throw new DataInputException("identifier code must be 3 numeric digits");
        }
    }
}
