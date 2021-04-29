package panteao.make.ready.utils.helpers.carousel.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class UserEntity {

    @SerializedName("user")
    @Expose
    private UserData user;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
