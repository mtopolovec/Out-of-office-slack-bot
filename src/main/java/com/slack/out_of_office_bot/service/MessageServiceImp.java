package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.slack.out_of_office_bot.constants.DateTimeFormatConstants.*;

/**
 * Service that handles all the messages that will be posted to user
 */
@Service
public class MessageServiceImp implements MessageService {

    /**
     * Error message to help user if he has a trouble composing and using /ooo command
     * @return few of examples how to compose a /ooo input message
     */
    @Override
    public String getSubmitErrorMessage() {
        return """
                Please check the correct pattern of the message!
                Examples:

                /ooo 10:00 - 13:00 (Today from 10-13h without reason)
                /ooo 10 - 13 (Today from 10-13h without reason)
                /ooo 12:00 - 15:00 Doctor appointment (Today from 12-15h with Reason: doctor appointment)
                /ooo 12 - 15 Doctor appointment (Today from 12-15h with Reason: doctor appointment)
                /ooo tomorrow 09:00 - 12:00 Getting car fixed (Tomorrow from 9-12h with Reason: Getting car fixed)
                /ooo tomorrow 09 - 12 Getting car fixed (Tomorrow from 9-12h with Reason: Getting car fixed)
                /ooo 25.09 09:00 - 12:00 Getting car fixed (25.09 from 9-12h with Reason: Getting car fixed)
                /ooo 25.09 09 - 12 Getting car fixed (25.09 from 9-12h with Reason: Getting car fixed)
                /ooo 10.08 - 25.08 Vacation (Out from 10 till 25 of august with Reason: Vacation)""";
    }

    @Override
    public String futureDateErrorMessage() {
        return "Ups we do not support past, please specify a date in the future!";
    }

    /**
     * Error message to help user if he has a trouble checking and using /ooo-check command
     * @return example how user should use the /ooo-check command
     */
    @Override
    public String checkCommandErrorMessage() {
        return """
               Please check the correct pattern of the check!
               /ooo-check @JaneDoe""";
    }

    /**
     * Error message to help user if he has a trouble removing and using /ooo-remove command
     * @return example how to remove his input and use /ooo-remove command
     */
    @Override
    public String removeCommandErrorMessage() {
        return """
                Please check the correct pattern of the remove!
                /ooo-remove 12:00 - 13:00
                /ooo-remove tomorrow 12:00 - 13:00""";
    }

    /**
     * User was not found message
     * @param userId users slack id
     * @return message that user was not found
     */
    @Override
    public String userNotFound(String userId) {
        return "Sorry <@" + userId + ">" + " was not found!";
    }

    /**
     * Users out of office message
     * @param userId users slack id
     * @param startDate users start date
     * @param endDate users end date
     * @param description users reason or description for going out of office
     * @return message that either has description if user has posted a description with his input or not if he didn't
     */
    @Override
    public String userOutOfOfficeMessage(String userId, LocalDateTime startDate, LocalDateTime endDate, String description) {
        if ((isItCurrentDate(startDate) || isItDateInFuture(startDate)) && (isItCurrentDate(endDate) || isItDateInFuture(endDate))) {
            if (description == null) {
                if (isItTomorrowDate(startDate) || isItTomorrowDate(endDate)) {
                    return "<@" + userId + ">"+
                            "\nIs out of office tomorrow from " + format(startDate) + " until " + format(endDate) +".";
                } else if (isWholeDay(startDate, endDate)) {
                    return "<@" + userId + ">"+
                            "\nIs out of office " + formatDate(startDate) + ".";
                } else if (startDate.toLocalDate().isEqual(endDate.toLocalDate()) && isItDateInFuture(startDate)) {
                    return "<@" + userId + ">"+
                            "\nIs out of office " + formatDate(startDate) + " from " + format(startDate) + " until " + format(endDate) +".";
                } else {
                    return "<@" + userId + ">"+
                            "\nIs out of office from " + format(startDate) + " until " + format(endDate) +".";
                }
            } else {
                if (isItTomorrowDate(startDate) || isItTomorrowDate(endDate)) {
                    return "<@" + userId + ">" +
                            "\nIs out of office for '" + description + "' tomorrow from " + format(startDate) + " until " + format(endDate) + ".";
                } else if (isWholeDay(startDate, endDate)) {
                    return "<@" + userId + ">"+
                            "\nIs out of office for '" + description + "' " + formatDate(startDate) + ".";
                } else if (startDate.toLocalDate().isEqual(endDate.toLocalDate()) && isItDateInFuture(startDate)){
                    return "<@" + userId + ">" +
                            "\nIs out of office for '" + description + "' " + formatDate(startDate) + " from " + format(startDate) + " until " + format(endDate) + ".";
                } else {
                    return "<@" + userId + ">" +
                            "\nIs out of office for '" + description + "' from " + format(startDate) + " until " + format(endDate) + ".";
                }
            }
        } else {
            if (startDate.toLocalDate().isEqual(endDate.toLocalDate())) {
                throw new DateTimeException("Date " + startDate.toLocalDate().format(DATE_FORMATTER) + " needs to be specified as date in the future.");
            } else {
                throw new DateTimeException("Dates " + startDate.toLocalDate().format(DATE_FORMATTER) + " or(and) "
                        + endDate.toLocalDate().format(DATE_FORMATTER) + " need to be specified as the current date or date in the future.");
            }
        }
    }

    /**
     * Method checks if date should have format for hours and minutes only or else it should have format for day and month
     * e.g. if it is date it is 10.08 if it is time then it is 13:00
     * @param date the date that you want check what format it needs to be
     * @return proper format for given date
     */
    private String format(LocalDateTime date) {
        // Checks if it has start time at midnight or 00:00 and end time at 23:59 if it has then it is date format
        // otherwise it is time format
        if (date.getHour() == 0 && date.getMinute() == 0 || date.getHour() == 23 && date.getMinute() == 59 && date.getSecond() == 59) {
            return formatDate(date);
        } else {
            return formatTime(date);
        }
    }

    /**
     * Method checks if the date is tomorrow
     * @param date the date that you want to check if it is tomorrow or not
     * @return true if it is tomorrow if not returns false
     */
    private Boolean isItTomorrowDate(LocalDateTime date) {
        return date.getDayOfMonth() == DateTimeUtils.currentTime().plusDays(1).getDayOfMonth();
    }

    /**
     * Method checks if the date is in the future
     * @param date the date that you want to check if it is date in the future or not
     * @return true if it is date in the future if not return false
     */
    private Boolean isItDateInFuture(LocalDateTime date) {
        return date.toLocalDate().isAfter(DateTimeUtils.currentTime().toLocalDate());
    }

    /**
     * Method checks if the date is current date
     * @param date the date that you want to check if it is current date or not
     * @return true if it is current date if not return false
     */
    private Boolean isItCurrentDate(LocalDateTime date) {
        return date.toLocalDate().isEqual(DateTimeUtils.currentTime().toLocalDate());
    }

    /**
     * Method checks if dates represent the one whole day
     * @param startDate the date that you want to check if it starts at midnight
     * @param endDate the date that you want to check if it ends in time just before midnight at the end of the day
     * @return true if it is whole day if not return false
     */
    private Boolean isWholeDay(LocalDateTime startDate, LocalDateTime endDate) {
        return (startDate.toLocalDate().equals(endDate.toLocalDate()) &&
                startDate.isEqual(startDate.toLocalDate().atStartOfDay()) && endDate.equals(endDate.toLocalDate().atTime(LocalTime.MAX)));
    }

    /**
     * Gets String format of LocalDateTime in date and time format e.g. 10-08-2021 | 13:00
     * @param date the date that we want to transform to String
     * @return String value in date and time format e.g. 10-08-2021 | 13:00
     */
    private String formatDateAndTime(LocalDateTime date) {
        return date.format(MESSAGE_DATE_AND_TIME_FORMAT);
    }

    /**
     * Gets String format of LocalDateTime in time format e.g. 13:00
     * @param date the date that we want to transform to String
     * @return String value in time format e.g. 13:00
     */
    private String formatTime(LocalDateTime date) {
        return date.format(MESSAGE_TIME_FORMAT);
    }

    /**
     * Gets String format of LocalDateTime in date format e.g. 10.08
     * @param date the date that we want to transform to String
     * @return String value in date format e.g. 10.08
     */
    private String formatDate(LocalDateTime date) {
        return date.format(MESSAGE_DATE_FORMAT);
    }

    /**
     * Message that user is available for whole day today
     * @param userId slack user id
     * @return message that user is available for whole day, so he won't be out of office
     */
    @Override
    public String msgUserIsAvailableForWholeDay(String userId) {
        return "<@" + userId + ">" + " is available for whole day.";
    }

    /**
     * Message that user is currently available but was out of office already earlier today
     * @param userId slack user id
     * @return message that user is currently available
     */
    @Override
    public String msgUserIsCurrentlyAvailable(String userId) {
        return "<@" + userId + ">" + " is currently available.";
    }

    /**
     * Message that user is currently out of office
     * @param userId slack user id
     * @param endTime when users out of office ends
     * @return message that user is currently unavailable, and he will be back at what time in the office
     */
    @Override
    public String msgUserIsCurrentlyOutOfOffice(String userId, LocalDateTime endTime) {
        return "<@" + userId + ">" + " is currently out of office and is back at " + format(endTime) + ".";
    }

    /**
     * Message that user is currently working but will be out of office in future time
     * @param userId slack user id
     * @param startTime when user out of office starts
     * @param endTime when user out of office ends
     * @return message that user is currently working but will be out of office from start time until end time
     */
    @Override
    public String msgUserIsOutFromTimeToTime(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return "<@" + userId + ">" + " is currently working and will be out of office from " + format(startTime) + " until " + format(endTime) + ".";
    }

    /**
     * Message that user is on vacation, and he won't be in the office for whole day
     * @param userId slack user id
     * @return message that user is on vacation, and he won't be in office for whole day
     */
    @Override
    public String msgUserIsOutWholeDay(String userId) {
        return "<@" + userId + ">" + " is on vacation and will be out of office whole day.";
    }

    /**
     * Message for successfully deleting user input
     * @param userId slack user id
     * @param startTime from what start time was out of office message
     * @param endTime until what end time was out of office message
     * @param description if there is some reason or description for out of office
     * @return message that user has successfully deleted his input for out of office
     * either with or without description that depends on that if user did post description/reason or not
     */
    @Override
    public String msgUserInputSuccessfullyDeleted(String userId, LocalDateTime startTime, LocalDateTime endTime, String description) {
        if (description == null) {
            if (isItTomorrowDate(startTime) || isItTomorrowDate(endTime)) {
                return "<@" + userId + ">" + " removed from tomorrow " + format(startTime) + " until " + format(endTime) + ".";
            } else {
                return "<@" + userId + ">" + " removed from " + format(startTime) + " until " + format(endTime) + ".";
            }
        } else {
            if (isItTomorrowDate(startTime) || isItTomorrowDate(endTime)) {
                return "<@" + userId + ">" + " removed '" + description + "' from tomorrow " + format(startTime) + " until " + format(endTime) + ".";
            } else {
                return "<@" + userId + ">" + " removed '" + description + "' from " + format(startTime) + " until " + format(endTime) + ".";
            }
        }
    }

    /**
     * Method if no inputs were found for some user for today
     * @param userId slack user id
     * @param startTime from what start time
     * @param endTime till what time
     * @return message for bot to post to user that is checking
     */
    @Override
    public String msgNoInputsForToday(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return "Sorry for " + "<@" + userId + ">" + " there are no out of office inputs for today " + format(startTime) + " - " + format(endTime) + ".";
    }
}
