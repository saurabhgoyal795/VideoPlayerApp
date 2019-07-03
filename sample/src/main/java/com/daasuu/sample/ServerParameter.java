package com.daasuu.sample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saurabhgoyal on 19/03/18.
 */

public class ServerParameter implements Parcelable {
    private String mKey;
    private String mValue;

    public ServerParameter(String text, String value) {
        mKey = text;
        mValue = value;
    }

    private ServerParameter(Parcel in) {
        mKey = in.readString();
        mValue = in.readString();
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        String str = mKey + ": " + mValue;
        return str;
    }

    @Override
    public int hashCode() {
        int hash = 123;
        hash += mKey.hashCode() + mValue.hashCode();
        return hash;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeString(mValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ServerParameter> CREATOR =
            new Parcelable.Creator<ServerParameter>() {
                public ServerParameter createFromParcel(Parcel source) {
                    return new ServerParameter(source);
                }

                public ServerParameter[] newArray(int size) {
                    return new ServerParameter[size];
                }
            };

}
