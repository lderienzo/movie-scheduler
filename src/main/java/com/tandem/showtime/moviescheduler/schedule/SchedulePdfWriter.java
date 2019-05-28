package com.tandem.showtime.moviescheduler.schedule;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tandem.showtime.moviescheduler.movie.Movie;
import com.tandem.showtime.moviescheduler.movie.Showing;
import com.tandem.showtime.moviescheduler.exceptions.SchedulePdfWriterException;

public class SchedulePdfWriter {
    private String error;
    private final Schedule weekdaySchedule;
    private final Schedule weekendSchedule;
    private static final String TANDEM_CINEMAS = "Tandem Cinemas Now Showing";
    private static final Logger LOG = LoggerFactory.getLogger(SchedulePdfWriter.class);
    private static final DateTimeFormatter AM_PM_FORMATTER = DateTimeFormat.forPattern("h:mm a");
    private static final Font TANDEM_CINEMAS_NOW_SHOWING_FONT = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.GRAY);
    private static final Font MOVIE_TITLE_HEADING_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
    private static final Font WEEKEND_WEEKDAY_HEADING_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font TABLE_CELL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);


    public SchedulePdfWriter(Schedule weekdaySchedule, Schedule weekendSchedule) {
        this.weekdaySchedule = weekdaySchedule;
        this.weekendSchedule = weekendSchedule;
    }

    public void writeSchedules(String outFilePath) {
        Document schedule = createPdfSchedule();
        openPdfScheduleForWriting(schedule, outFilePath);
        PdfPTable pdfTable = createPdfTable();
        addTandemCinimasHeadingRow(pdfTable);
        for (int movieNumber = 0; movieNumber < numberOfMoviesInWeekendShowings(); movieNumber ++) {
            int numOfWeekdayShowings = getWeekdayMovieForMovieNumber(movieNumber).weekdayShowings().size();
            int numOfWeekendShowings = getWeekendMovieForMovieNumber(movieNumber).weekendShowings().size();
            int numberOfAdditionalWeekendShowings = numOfWeekendShowings - numOfWeekdayShowings;
            Movie weekendMovie = getWeekendMovieForMovieNumber(movieNumber);
            addMovieTitleHeadingRow(pdfTable, weekendMovie.titleWithRatingForSchedule());
            addWeekdayAndWeekendHeadingsRow(pdfTable);
            for (int showingNumber = weekendMovie.weekendShowings().size() - 1; showingNumber >= 0 ; showingNumber--) {
                Showing weekdayShowing;
                String weekdayStartTime = "";
                if ((showingNumber - numberOfAdditionalWeekendShowings <= numOfWeekdayShowings) && (numOfWeekdayShowings != 0)) {
                    weekdayShowing = getWeekdayMovieForMovieNumber(movieNumber).weekdayShowings().get(showingNumber - numberOfAdditionalWeekendShowings);
                    weekdayStartTime = AM_PM_FORMATTER.print(weekdayShowing.startTime()) + " - " + AM_PM_FORMATTER.print(weekdayShowing.endTime());
                    numOfWeekdayShowings--;
                }
                Showing weekendShowing =  getWeekendMovieForMovieNumber(movieNumber).weekendShowings().get(showingNumber);
                String weekendStartTime = AM_PM_FORMATTER.print(weekendShowing.startTime()) + " - " + AM_PM_FORMATTER.print(weekendShowing.endTime());
                addShowingTimesRow(new PdfPCell(), weekdayStartTime, weekendStartTime, pdfTable);
            }
        }
        addPdfTableToSchedule(schedule, pdfTable);
        closeSchedule(schedule);
    }

    private Movie getWeekdayMovieForMovieNumber(int movieNumber) {
        return weekdaySchedule.moviesPlaying().get(movieNumber);
    }

    private Movie getWeekendMovieForMovieNumber(int movieNumber) {
        return weekendSchedule.moviesPlaying().get(movieNumber);
    }

    private int numberOfMoviesInWeekendShowings() {
        return weekendSchedule.moviesPlaying().size();
    }

    private Document createPdfSchedule() {
        return new Document();
    }

    private void openPdfScheduleForWriting(Document document, String outFilePath) {
        getPdfWriterInstance(document, outFilePath);
        document.open();
    }

    private void getPdfWriterInstance(Document document, String outFilePath) {
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outFilePath));
        } catch (DocumentException | FileNotFoundException e) {
            logError(e.getMessage());
            throw new SchedulePdfWriterException(error);
        }
    }

    private PdfPTable createPdfTable() {
        PdfPTable table = new PdfPTable(new float[] { 5, 4, 5});
        table.setWidthPercentage(60);
        return table;
    }

    private void addTandemCinimasHeadingRow(PdfPTable table) {
        PdfPCell cell = new PdfPCell(new Phrase(TANDEM_CINEMAS, TANDEM_CINEMAS_NOW_SHOWING_FONT));
        cell = setTitleHeadingRowCellAttributes(cell);
        table.addCell(cell);
    }

    private void addMovieTitleHeadingRow(PdfPTable table, String movieTitle) {
        PdfPCell cell = new PdfPCell(new Phrase(movieTitle, MOVIE_TITLE_HEADING_FONT));
        cell = setTitleHeadingRowCellAttributes(cell);
        table.addCell(cell);
    }

    private PdfPCell setTitleHeadingRowCellAttributes(PdfPCell cell) {
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(3);
        cell.setFixedHeight(72f);
        cell.setPaddingBottom(10);
        cell.setPaddingRight(35);
        return cell;
    }

    private void addWeekdayAndWeekendHeadingsRow(PdfPTable table) {
        addWeekdayWeekendHeadingCell(table, "Weekday");
        addWeekdayWeekendHeadingCell(table, "");
        addWeekdayWeekendHeadingCell(table, "Weekend");
    }

    private void addWeekdayWeekendHeadingCell(PdfPTable table, String headingText) {
        PdfPCell cell = new PdfPCell(new Phrase(headingText, WEEKEND_WEEKDAY_HEADING_FONT));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addShowingTimesRow(PdfPCell cell, String weekdayStartTime, String weekendStartTime, PdfPTable table) {
        addShowingTimeCell(cell, weekdayStartTime, table);
        addShowingTimeCell(cell, "", table);
        addShowingTimeCell(cell, weekendStartTime, table);
    }

    private void addShowingTimeCell(PdfPCell cell, String text, PdfPTable table) {
        cell.setPhrase(new Phrase(text, TABLE_CELL_FONT));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addPdfTableToSchedule(Document document, PdfPTable table) {
        try {
            document.add(table);
        } catch (DocumentException e) {
            logError(e.getMessage());
            throw new SchedulePdfWriterException(error);
        }
    }

    private void closeSchedule(Document document) {
        document.close();
    }

    private void logError(String errorMsg) {
        setErrorMemberWithErrorMessage(errorMsg);
        LOG.error(error);
    }

    private void setErrorMemberWithErrorMessage(String errorMsg) {
        error = errorMsg;
    }
}
