package com.tandem.showtime.moviescheduler.arguments;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;


public abstract class AbstractDataLoader<T> implements ArgDataLoader {

    protected ApplicationArguments args;


    protected final String extractValueForArgument(ArgOption argName) {
        validateThatRequiredArgIsPresent(argName);
        return extractValue(argName);
    }

    private void validateThatRequiredArgIsPresent(ArgOption argName) {
        if (Arrays.stream(args.getSourceArgs())
                .filter(argNameAndValue -> argNameAndValue.contains(argName.toString())).count() != 1)
        throw new ArgsProcessorException("Wrong number of required args present.");
    }

    private String extractValue(ArgOption argName) {
        return Arrays.stream(args.getSourceArgs())
                .filter(argNameAndValue -> argNameAndValue.contains(argName.toString()))
                .map(argNameAndValue -> splitArgNameAndValue(argNameAndValue))
                .collect(Collectors.joining());
    }

    private String splitArgNameAndValue(String argNameAndValue) {
        String[] splitArg = argNameAndValue.split("=");
        if (splitArg.length != 2)
            throw new ArgsProcessorException("Missing argument value.");
        return splitArg[1];
    }
}
