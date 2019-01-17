package mongoose.client.businesslogic.pricing;

import mongoose.client.businessdata.workingdocument.WorkingDocumentLine;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
final class AttendanceBlock implements Comparable<AttendanceBlock> {

    private final WorkingDocumentLine workingDocumentLine;
    private final LocalDate date;
    private int price;


    AttendanceBlock(WorkingDocumentLine workingDocumentLine, LocalDate date) {
        this.workingDocumentLine = workingDocumentLine;
        this.date = date;
    }

    WorkingDocumentLine getWorkingDocumentLine() {
        return workingDocumentLine;
    }

    LocalDate getDate() {
        return date;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    int getPrice() {
        return price;
    }

    @Override
    public int compareTo(AttendanceBlock that) {
        int result = date.compareTo(that.date);
        if (result == 0)
            result = workingDocumentLine.firstDate().compareTo(that.workingDocumentLine.firstDate());
        return result;
    }
}
