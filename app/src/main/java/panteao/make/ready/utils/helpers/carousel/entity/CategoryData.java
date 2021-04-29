package panteao.make.ready.utils.helpers.carousel.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryData implements Parcelable {

    public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel parcel) {
            return new CategoryData(parcel);
        }

        @Override
        public CategoryData[] newArray(int i) {
            return new CategoryData[i];
        }
    };
    private int categoryId;
    private String name;
    private String status;

    private CategoryData(Parcel parcel) {
        categoryId = parcel.readInt();
        name = parcel.readString();
        status = parcel.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int id) {
        this.categoryId = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeString(name);
        dest.writeString(status);
    }
}
