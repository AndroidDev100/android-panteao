package panteao.make.ready.utils.cropImage.helpers;

import android.app.Activity;

import panteao.make.ready.beanModel.beanModel.SectionDataModel;
import panteao.make.ready.beanModel.beanModel.SingleItemModel;
import panteao.make.ready.utils.helpers.carousel.model.Slide;

import java.util.ArrayList;

import panteao.make.ready.utils.helpers.carousel.model.Slide;

public class ShimmerDataModel {
    ArrayList<SectionDataModel> allSampleData;

    public ShimmerDataModel(Activity activity) {

    }

    public ArrayList<SectionDataModel> getList(int type) {
        allSampleData = new ArrayList<>();
        SectionDataModel dmm;
        ArrayList<SingleItemModel> singleItem;
        switch (type) {

            case 3:
                // for square & cirle listing
                dmm = new SectionDataModel();
                dmm.setType(1);
                dmm.setHeaderTitle("Section " + 1);
                singleItem = new ArrayList<>();
                for (int j = 0; j <= 15; j++) {
                    singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                }

                dmm.setAllItemsInSection(singleItem);
                allSampleData.add(dmm);


                break;
            case 4:
                // for landscape
                dmm = new SectionDataModel();
                dmm.setType(3);
                dmm.setHeaderTitle("Section " + 3);
                singleItem = new ArrayList<>();
                for (int j = 0; j <= 15; j++) {
                    singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                }

                dmm.setAllItemsInSection(singleItem);
                allSampleData.add(dmm);


                break;
            case 5:
                // for potrait
                dmm = new SectionDataModel();
                dmm.setType(2);
                dmm.setHeaderTitle("Section " + 2);
                singleItem = new ArrayList<>();
                for (int j = 0; j <= 15; j++) {
                    singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                }

                dmm.setAllItemsInSection(singleItem);
                allSampleData.add(dmm);


                break;
            case 1:
                for (int i = 1; i <= 5; i++) {

                    SectionDataModel dm = new SectionDataModel();
                    if (i == 1) {
                        dm.setType(2);
                    } else if (i == 2) {
                        dm.setType(2);
                    } else if (i == 3) {
                        dm.setType(2);

                    } else if (i == 4) {
                        dm.setType(2);
                    } else if (i == 5) {
                        dm.setType(2);
                    }

                    dm.setHeaderTitle("Section " + i);
                    singleItem = new ArrayList<>();
                    for (int j = 0; j <= 5; j++) {
                        singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                    }

                    dm.setAllItemsInSection(singleItem);
                    allSampleData.add(dm);

                }
                break;
            case 2:
                for (int i = 1; i <= 5; i++) {

                    SectionDataModel dm = new SectionDataModel();
                    if (i == 1) {
                        dm.setType(1);
                    } else if (i == 2) {
                        dm.setType(4);
                    } else if (i == 3) {
                        dm.setType(3);

                    } else if (i == 4) {
                        dm.setType(2);
                    } else if (i == 5) {
                        dm.setType(1);
                    }

                    dm.setHeaderTitle("Section " + i);
                    singleItem = new ArrayList<>();
                    for (int j = 0; j <= 5; j++) {
                        singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                    }

                    dm.setAllItemsInSection(singleItem);
                    allSampleData.add(dm);

                }
                break;
            default:
                for (int i = 1; i <= 5; i++) {

                    SectionDataModel dm = new SectionDataModel();
                    if (i == 1) {
                        dm.setType(0);
                    } else if (i == 2) {
                        dm.setType(4);
                    } else if (i == 3) {
                        //dm.setType(3);
                        dm.setType(4);
                    } else if (i == 4) {
                        dm.setType(2);
                    } else if (i == 5) {
                        dm.setType(1);
                    }

                    dm.setHeaderTitle("Section " + i);
                    singleItem = new ArrayList<>();
                    for (int j = 0; j <= 5; j++) {
                        singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
                    }

                    dm.setAllItemsInSection(singleItem);
                    allSampleData.add(dm);
                }

                break;
        }


        return allSampleData;
    }

    public ArrayList<Slide> getSlides() {
        ArrayList<Slide> slides = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Slide slide = new Slide();
            //slide.setImageResource(R.drawable.banner_img);
            slide.setType(1);
            slides.add(slide);
        }
        return slides;
    }
}
