package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import java.util.Objects;

public class DailyWashContent {
    public enum Type {DOC, NAME}

    public final String fileName;
    public final Type type;
    public final String content;
    public final int contentSizeInBytes;

    public DailyWashContent(String fileName, Type type, String content) {
        this.fileName = fileName;
        this.type = type;
        this.content = content;
        this.contentSizeInBytes = content.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWashContent that = (DailyWashContent) o;
        return contentSizeInBytes == that.contentSizeInBytes &&
                Objects.equals(fileName, that.fileName) &&
                type == that.type &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fileName, type, content, contentSizeInBytes);
    }

    @Override
    public String toString() {
        return "DailyWashContent{" +
                "fileName='" + fileName + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", contentSizeInBytes=" + contentSizeInBytes +
                '}';
    }
}
