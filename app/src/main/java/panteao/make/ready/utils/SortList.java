package panteao.make.ready.utils;


import panteao.make.ready.beanModel.KeywordList;

import java.util.Comparator;

import panteao.make.ready.beanModel.KeywordList;

class SortList implements Comparator<KeywordList> {

    @Override
    public int compare(KeywordList searchedKeywords, KeywordList t1) {

        return (int) (searchedKeywords.getTimeStamp() - t1.getTimeStamp());
    }
}
