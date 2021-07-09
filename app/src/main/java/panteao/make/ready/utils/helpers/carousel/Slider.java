package panteao.make.ready.utils.helpers.carousel;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.make.enums.CRIndicator;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.carousel.adapter.SliderAdapter;
import panteao.make.ready.utils.helpers.carousel.indicators.IndicatorShape;
import panteao.make.ready.utils.helpers.carousel.indicators.SlideIndicatorsGroup;
import panteao.make.ready.R;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Random;


public class Slider extends FrameLayout implements ViewPager.OnPageChangeListener {

        public static final String TAG = "SLIDER";
        private final Handler handler = new Handler();
        private LooperWrapViewPager viewPager;
        private AdapterView.OnItemClickListener itemClickListener;
        private Drawable selectedSlideIndicator;
        private Drawable unSelectedSlideIndicator;
        private int defaultIndicator;
        private int indicatorSize;
        private boolean mustAnimateIndicators;
        private boolean mustLoopSlides;
        private SlideIndicatorsGroup slideIndicatorsGroup;
        private int slideShowInterval = 4000;
        private int slideCount;
        private int currentPageNumber;
        private boolean hideIndicators = false;
        private int layoutType = -1;

        public Slider(@NonNull Context context) {
            super(context);
        }

        AttributeSet attrset;
        public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            attrset=attrs;

        }


        public Slider(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            attrset=attrs;
        }

        private void parseCustomAttributes(AttributeSet attributeSet) {
            try {
                if (attributeSet != null) {
                    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.BannerSlider);
                    try {
                        indicatorSize = (int) typedArray.getDimension(R.styleable.BannerSlider_indicatorSize, getResources().getDimensionPixelSize(R.dimen.default_indicator_size));
                        selectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_selected_slideIndicator);
                        unSelectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_unselected_slideIndicator);
                        defaultIndicator = typedArray.getInt(R.styleable.BannerSlider_defaultIndicators, IndicatorShape.CIRCLE);
                        mustAnimateIndicators = typedArray.getBoolean(R.styleable.BannerSlider_animateIndicators, true);
                        mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, true);
                        hideIndicators = typedArray.getBoolean(R.styleable.BannerSlider_hideIndicators, false);
                        Log.w("carousalValue", KsPreferenceKeys.getInstance().getAutoRotation()+" "+KsPreferenceKeys.getInstance().getAutoDuration()+" "+mustLoopSlides);
                        if (KsPreferenceKeys.getInstance().getAutoRotation()){
                            if (KsPreferenceKeys.getInstance().getAutoDuration()>0){
                                int slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, KsPreferenceKeys.getInstance().getAutoDuration());
                                Log.w("carousalValue++",slideShowIntervalSecond+"");
                                slideShowInterval = slideShowIntervalSecond * 1000;
                                Log.w("carousalValue++--",slideShowInterval+"");
                            }else {
                                int slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, 3);
                                slideShowInterval = slideShowIntervalSecond * 1000;
                            }
                        }



                    } catch (Exception e) {

                        e.printStackTrace();
                    } finally {
                        typedArray.recycle();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean autoRotate=true;
        int autoRotateDuration=0;
        public void addSlides(RailCommonData slideList, CommonRailtItemClickListner listner, int position, int layoutType, String indicatorPosition,boolean auto,int rotateDuration) {
            parseCustomAttributes(attrset);
            if (slideList == null || slideList.getEnveuVideoItemBeans().size() == 0)
                return;
            viewPager = new LooperWrapViewPager(getContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                viewPager.setId(View.generateViewId());
            } else {
                int id = Math.abs(new Random().nextInt((5000 - 1000) + 1) + 1000);
                viewPager.setId(id);
            }
            viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewPager.addOnPageChangeListener(Slider.this);
            addView(viewPager);
            SliderAdapter adapter = new SliderAdapter(getContext(), slideList, layoutType, listner,position );
            viewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            slideCount = slideList.getEnveuVideoItemBeans().size();
            viewPager.setCurrentItem(0);
            viewPager.setClipToPadding(false);
     //     viewPager.setBackgroundColor(getResources().getColor(R.color.black_theme_color));
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            autoRotate=auto;
            autoRotateDuration=rotateDuration;

            if (tabletSize) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    viewPager.setPadding(getViewPagerPadding(layoutType), 5, getViewPagerPadding(layoutType), 10);
                } else {
                    viewPager.setPadding(getViewPagerTabletPadding(layoutType), 5, getViewPagerTabletPadding(layoutType), 10);

                    // viewPager.setPadding(getViewPagerTabletPadding(layoutType), 10, getViewPagerTabletPadding(layoutType), 10);
                }
            } else {
                viewPager.setPadding(0, 10, 0, 10);
            }


            viewPager.setPageMargin(20);

            if (indicatorPosition.equalsIgnoreCase(CRIndicator.HDN.name())) {
                hideIndicators = true;
            }
            if (!hideIndicators && slideCount > 1) {
                slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators, layoutType, indicatorPosition);
                slideIndicatorsGroup.setBackgroundColor(getResources().getColor(R.color.more_text_color_dark));
                addView(slideIndicatorsGroup);
                slideIndicatorsGroup.setSlides(slideCount);
                slideIndicatorsGroup.onSlideChange(0);
                slideIndicatorsGroup.setBackgroundColor(getContext().getResources().getColor(R.color.transparentColor));

            }
            if (slideCount > 1){
                Log.w("carousalValue--",auto+"");
                if (autoRotate){
                    setupTimer();
                }
            }
        }
        private int getViewPagerTabletPadding(int layoutType) {
            switch (layoutType) {
                case AppConstants
                        .CAROUSEL_LDS_LANDSCAPE:
                    return 0;
                case AppConstants
                        .CAROUSEL_PR_POTRAIT:
                    return (int) getResources().getDimension(R.dimen.carousal_potrait_padding);
                case AppConstants
                        .CAROUSEL_SQR_SQUARE:
                    return (int) 200;
                case AppConstants.CAROUSEL_CIR_CIRCLE:
                    return AppCommonMethod.convertDpToPixel(40);
                default:
                    return 0;
            }
        }
        private int getViewPagerPadding(int layoutType) {
            switch (layoutType) {
                case AppConstants
                        .CAROUSEL_LDS_LANDSCAPE:
                    return (int) getResources().getDimension(R.dimen.carousal_landscape_padding);
                case AppConstants
                        .CAROUSEL_PR_POTRAIT:
                    return (int) getResources().getDimension(R.dimen.carousal_potrait_padding);
                case AppConstants
                        .CAROUSEL_SQR_SQUARE:
                    return (int) getResources().getDimension(R.dimen.carousal_square_padding);
                case AppConstants.CAROUSEL_CIR_CIRCLE:
                    return AppCommonMethod.convertDpToPixel(40);
                default:
                    return 0;
            }
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPageNumber = position;
            if (slideIndicatorsGroup != null && !hideIndicators) {
                if (position == 0) {
                    slideIndicatorsGroup.onSlideChange(slideCount - 1);
                } else if (position == slideCount + 1) {
                    slideIndicatorsGroup.onSlideChange(0);
                } else {
                    slideIndicatorsGroup.onSlideChange(position - 1);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    if (autoRotate){
                        setupTimer();
                    }
                    break;
            }
        }

        private void setupTimer() {
            try {
                if (mustLoopSlides) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (currentPageNumber < slideCount)
                                    currentPageNumber += 1;
                                else
                                    currentPageNumber = 1;

                                viewPager.setCurrentItem(currentPageNumber - 1, true);

                                handler.removeCallbacksAndMessages(null);
                                handler.postDelayed(this, slideShowInterval);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, slideShowInterval);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // setters
        public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setHideIndicators(boolean hideIndicators) {
            this.hideIndicators = hideIndicators;
            try {
                if (hideIndicators)
                    slideIndicatorsGroup.setVisibility(INVISIBLE);
                else
                    slideIndicatorsGroup.setVisibility(VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

