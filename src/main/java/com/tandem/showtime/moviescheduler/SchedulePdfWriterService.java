package com.tandem.showtime.moviescheduler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

public class SchedulePdfWriterService {
    private Schedule weekdaySchedule;
    private Schedule weekendSchedule;
    private static final DateTimeFormatter AM_PM_FORMATTER = DateTimeFormat.forPattern("h:mm a");
    private static final Font MOVIE_TITLE_HEADING_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
    private static final Font WEEKEND_WEEKDAY_HEADING_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font TABLE_CELL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

    public SchedulePdfWriterService(Schedule weekdaySchedule, Schedule weekendSchedule) {
        this.weekdaySchedule = weekdaySchedule;
        this.weekendSchedule = weekendSchedule;
    }

    public void writeSchedules() throws FileNotFoundException, DocumentException {  // TODO: make this a RuntimeException
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("/Users/lderienzo/Downloads/tandem_cinemas_showing_schedule.pdf"));

        document.open();

        int numberOfColumns = 3;
        PdfPTable table = new PdfPTable(new float[] { 5, 4, 5});
        table.setWidthPercentage(60);
        PdfPCell cell = null;
        // use number of movies of weekend schedule to do main loop since both have same number of movies
        for (int j = 0; j <  weekendSchedule.moviesPlaying().size(); j ++) {

            Movie weekendMovie = weekendSchedule.moviesPlaying().get(j);

            // determine number of showings here...
            int numOfWeekdayShowings = weekdaySchedule.moviesPlaying().get(j).weekdayShowings().size();
            int numOfWeekendShowings = weekendSchedule.moviesPlaying().get(j).weekendShowings().size(); // either the same or more than weekday
            int howManyMoreWeekendShowsThanWeekday = numOfWeekendShowings - numOfWeekdayShowings;

//            cell = new PdfPCell(new Phrase());
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setFixedHeight(52f);
//            table.addCell(cell);

            cell = new PdfPCell(new Phrase(weekendMovie.titleWithRatingForSchedule(), MOVIE_TITLE_HEADING_FONT));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            cell.setFixedHeight(72f);
            cell.setPaddingBottom(10);
            cell.setPaddingRight(35);
            table.addCell(cell);

//            cell.setPhrase(new Phrase());
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setFixedHeight(52f);
//            table.addCell(cell);

            // ---- new row ------

            cell = new PdfPCell(new Phrase("Weekday", WEEKEND_WEEKDAY_HEADING_FONT));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell.setPhrase(new Phrase());
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell.setPhrase(new Phrase("Weekend", WEEKEND_WEEKDAY_HEADING_FONT));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            // ---- new row ------

            for (int i = weekendMovie.weekendShowings().size()-1; i >= 0 ; i--) {

                // however many weekend showings there are, subtract weekday showings from it and add 1

                Showing weekdayShowing; // = new Showing();
                String weekdayStartTime = "";
                if (i-howManyMoreWeekendShowsThanWeekday <= numOfWeekdayShowings && numOfWeekdayShowings != 0) {
                    weekdayShowing = weekdaySchedule.moviesPlaying().get(j).weekdayShowings().get(i-howManyMoreWeekendShowsThanWeekday); // remember, this will have fewer showings
                    weekdayStartTime = AM_PM_FORMATTER.print(weekdayShowing.startTime()) + " - " + AM_PM_FORMATTER.print(weekdayShowing.endTime());
                    numOfWeekdayShowings--;
                }

                Showing weekendShowing =  weekendSchedule.moviesPlaying().get(j).weekendShowings().get(i);
                String weekendStartTime = AM_PM_FORMATTER.print(weekendShowing.startTime()) + " - " + AM_PM_FORMATTER.print(weekendShowing.endTime());

                cell.setPhrase(new Phrase(weekdayStartTime, TABLE_CELL_FONT));
                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setFixedHeight(36f);
                table.addCell(cell);

                cell.setPhrase(new Phrase());
                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setFixedHeight(36f);
                table.addCell(cell);

                cell.setPhrase(new Phrase(weekendStartTime, TABLE_CELL_FONT));
                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setFixedHeight(36f);
                table.addCell(cell);
            }
        }
        document.add(table);

        document.close();
    }

}
