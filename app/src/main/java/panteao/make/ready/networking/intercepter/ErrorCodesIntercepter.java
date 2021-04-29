package panteao.make.ready.networking.intercepter;


import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.beanModel.purchaseModel.PurchaseResponseModel;
import panteao.make.ready.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import panteao.make.ready.beanModel.responseIsLike.ResponseIsLike;
import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel;
import panteao.make.ready.beanModel.responseModels.SignUp.SignupResponseAccessToken;
import panteao.make.ready.redeemcoupon.RedeemCouponResponseModel;
import panteao.make.ready.repository.redeemCoupon.RedeemModel;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.beanModel.userProfile.UserProfileResponse;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.json.JSONObject;

import retrofit2.Response;

public class ErrorCodesIntercepter {

    private static ErrorCodesIntercepter instance;
    static PanteaoApplication appInstance;
    static int ErrorCode409 = 409;
    static int ErrorCode400 = 400;


    private ErrorCodesIntercepter() {
    }

    public static ErrorCodesIntercepter getInstance() {

        if (instance == null) {
            instance = new ErrorCodesIntercepter();
            appInstance = PanteaoApplication.getInstance();
        }
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        }
        return (instance);
    }


    public SignupResponseAccessToken manualSignUp(Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel> response) {
        SignupResponseAccessToken responseModel = null;
        if (response.code() == ErrorCode409) {
            LoginResponseModel cl = new LoginResponseModel();
            cl.setResponseCode(4901);
            String debugMessage = "";

            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                debugMessage = jObjError.getString("debugMessage");
                Logger.e("", "" + jObjError.getString("debugMessage"));

                responseModel = new SignupResponseAccessToken();
                if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                    responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_already_exists​));
                } else {
                    responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_already_exists​));
                }

            } catch (Exception e) {
                Logger.e("RegistrationLoginRepo", "" + e.toString());
            }


            responseModel.setResponseModel(cl);

        } else if (response.code() == ErrorCode400) {
            LoginResponseModel cl = new LoginResponseModel();
            cl.setResponseCode(400);
            responseModel = new SignupResponseAccessToken();
            try {
                JSONObject errorObject = new JSONObject(response.errorBody().string());
                if (errorObject.getInt("responseCode") != 0) {
                    if (errorObject.getInt("responseCode") == 4003) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.password_cannot_be_blank));

                    } else if (errorObject.getInt("responseCode") == 4004) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.please_provide_valid_name));

                    } else if (errorObject.getInt("responseCode") == 4005) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.email_id_cannot_be_blank));

                    } else {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong));

                    }
                } else {
                    responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong));

                }

                responseModel.setResponseModel(cl);


            } catch (Exception e) {
                responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong));

                responseModel.setResponseModel(cl);
            }
        }
        return responseModel;
    }

    public LoginResponseModel fbLogin(Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel> response) {
        LoginResponseModel cl = null;
        try {
            cl = new LoginResponseModel();
            JSONObject errorObject = new JSONObject(response.errorBody().string());

            if (errorObject.getInt("responseCode") != 0) {
                if (errorObject.getInt("responseCode") == 4301) {
                    cl.setResponseCode(403);
                    cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.either_user_email_not_found));

                } else if (errorObject.getInt("responseCode") == 4103) {
                    cl.setResponseCode(400);
                    cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_deactivated));

                } else if (errorObject.getInt("responseCode") == 4901) {
                    cl.setResponseCode(400);
                    cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_already_exists));

                } else if (errorObject.getInt("responseCode") == 4007) {
                    cl.setResponseCode(400);
                    cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.fb_id_cannot_be_null_or_empty));

                } else if (errorObject.getInt("responseCode") == 500) {
                    cl.setResponseCode(400);
                    cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));

                } else {
                    cl.setResponseCode(400);
                    cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));

                }
            }
        } catch (Exception e) {
            cl.setResponseCode(400);
            cl.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }

        return cl;
    }

    public LoginResponseModel Login(Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel> response) {
        LoginResponseModel responseModel = null;
        try {
            responseModel = new LoginResponseModel();
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            responseModel.setResponseCode(400);
            if (errorObject.getInt("responseCode") != 0) {
                if (errorObject.getInt("responseCode") != 0) {
                    if (errorObject.getInt("responseCode") == 4003) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.password_cannot_be_blank));

                    } else if (errorObject.getInt("responseCode") == 4004) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.please_provide_valid_name));

                    } else if (errorObject.getInt("responseCode") == 4401) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_does_not_exists));

                    } else if (errorObject.getInt("responseCode") == 4103) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_deactivated));

                    } else if (errorObject.getInt("responseCode") == 4006) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_can_not_login));

                    } else if (errorObject.getInt("responseCode") == 4002) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.username_password_doest_match));

                    } else if (errorObject.getInt("responseCode") == 4005) {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.email_id_cannot_be_blank));
                    } else {
                        responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong));

                    }
                } else {
                    responseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong));
                }
            }
        } catch (Exception e) {

        }

        return responseModel;
    }

    public ResponseEntitle checkEntitlement(Response<ResponseEntitle> response) {
        ResponseEntitle responseEntitlement = new ResponseEntitle();
        try {

            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4406) {
                    responseEntitlement.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.no_valid_offer));
                    responseEntitlement.setStatus(false);
                } else if (code == 4001) {
                    responseEntitlement.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.no_Subscription_managment));
                    responseEntitlement.setStatus(false);
                } else if (code == 4302) {
                    responseEntitlement.setResponseCode(4302);
                    Logger.w("languageValeu-->>", PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                    responseEntitlement.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    responseEntitlement.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.server_error));
                    responseEntitlement.setStatus(false);
                }

            } else {
                responseEntitlement.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                responseEntitlement.setStatus(false);
            }


        } catch (Exception ignored) {
            responseEntitlement.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
            responseEntitlement.setStatus(false);
        }
        return responseEntitlement;
    }


    public ResponseMembershipAndPlan checkPlans(Response<ResponseMembershipAndPlan> response, ResponseMembershipAndPlan responseEntitlement) {
        return null;
    }

    public UserProfileResponse userProfile(Response<com.make.userManagement.bean.UserProfile.UserProfileResponse> response) {
        UserProfileResponse empty = new UserProfileResponse();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4302) {
                    empty.setStatus(false);
                    empty.setResponseCode(4302);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    empty.setStatus(false);
                    empty.setResponseCode(500);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }
        } catch (Exception ignored) {
            empty.setResponseCode(500);
            empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return empty;
    }

    public ResponseEmpty addToWatchlisht(Response<ResponseEmpty> response) {
        ResponseEmpty empty = new ResponseEmpty();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4904) {
                    empty.setStatus(false);
                    empty.setResponseCode(4904);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_already_in_watchlist));
                }if (code == 4408) {
                    empty.setStatus(false);
                    empty.setResponseCode(4408);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_not_already_in_watchlist));
                } else if (code == 4302) {
                    empty.setStatus(false);
                    empty.setResponseCode(4302);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    empty.setStatus(false);
                    empty.setResponseCode(500);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }
        } catch (Exception ignored) {
            empty.setResponseCode(500);
            empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return empty;
    }

    public RedeemModel redeemCoupon(Response<RedeemCouponResponseModel> response) {
        RedeemModel empty = new RedeemModel();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4903) {
                    empty.setStatus(false);
                    empty.setResponseCode(4903);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.subscribed_already));
                } else if (code == 4044) {
                    empty.setStatus(false);
                    empty.setResponseCode(4044);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.redeem_code_already_used));
                } else if (code == 4046) {
                    empty.setStatus(false);
                    empty.setResponseCode(4046);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.redeem_cancelled));
                } else if (code == 4045) {
                    empty.setStatus(false);
                    empty.setResponseCode(4045);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.redeem_code_invalid));
                } else if (code == 4047) {
                    empty.setStatus(false);
                    empty.setResponseCode(4047);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.redeem_code_expired));
                } else if (code == 4049) {
                    empty.setStatus(false);
                    empty.setResponseCode(4049);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.please_try));
                } else if (code == 4404) {
                    empty.setStatus(false);
                    empty.setResponseCode(4404);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.cannot_find_other_offer));
                } else if (code == 4405) {
                    empty.setStatus(false);
                    empty.setResponseCode(4405);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.not_find_any_offer));
                } else if (code == 4409) {
                    empty.setStatus(false);
                    empty.setResponseCode(4409);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.currency_not_supported));
                } else if (code == 4410) {
                    empty.setStatus(false);
                    empty.setResponseCode(4410);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.cannot_find_any_subs));
                }else if (code == 4302) {
                    empty.setStatus(false);
                    empty.setResponseCode(4302);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                }
                else if (code == 500) {
                    empty.setStatus(false);
                    empty.setResponseCode(500);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }
        } catch (Exception ignored) {
            empty.setResponseCode(500);
            empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return empty;
    }

    public ResponseEmpty likeAsset(Response<ResponseEmpty> response) {
        ResponseEmpty empty = new ResponseEmpty();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4902) {
                    empty.setStatus(false);
                    empty.setResponseCode(4902);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_already_liked));
                }else if (code == 4403) {
                    empty.setStatus(false);
                    empty.setResponseCode(4403);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.user_already_liked));
                } else if (code == 4302) {
                    empty.setStatus(false);
                    empty.setResponseCode(4302);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    empty.setStatus(false);
                    empty.setResponseCode(500);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }
        } catch (Exception ignored) {
            empty.setResponseCode(500);
            empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return empty;
    }


    public ResponseGetIsWatchlist isWatchlist(Response<ResponseGetIsWatchlist> response) {
        ResponseGetIsWatchlist empty = new ResponseGetIsWatchlist();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4302) {
                    empty.setStatus(false);
                    empty.setResponseCode(4302);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    empty.setStatus(false);
                    empty.setResponseCode(500);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }
        } catch (Exception ignored) {
            empty.setResponseCode(500);
            empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return empty;
    }

    public ResponseIsLike isLike(Response<ResponseIsLike> response) {
        ResponseIsLike empty = new ResponseIsLike();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4302) {
                    empty.setStatus(false);
                    empty.setResponseCode(4302);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    empty.setStatus(false);
                    empty.setResponseCode(500);
                    empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }
        } catch (Exception ignored) {
            empty.setResponseCode(500);
            empty.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return empty;
    }

    public PurchaseResponseModel createNewOrder(Response<PurchaseResponseModel> response) {
        PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
        try {

            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4414) {
                    purchaseResponseModel.setResponseCode(4414);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.payment_config_not_found));
                } else if (code == 4404) {
                    purchaseResponseModel.setResponseCode(4404);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.cannot_find_other_offer));
                } else if (code == 4405) {
                    purchaseResponseModel.setResponseCode(4405);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.not_find_any_offer));
                } else if (code == 4409) {
                    purchaseResponseModel.setResponseCode(4409);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.currency_not_supported));
                } else if (code == 4410) {
                    purchaseResponseModel.setResponseCode(4410);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.not_find_any_offer));
                } else if (code == 4302) {
                    purchaseResponseModel.setResponseCode(4302);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }else {
                    purchaseResponseModel.setResponseCode(4405);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }

        } catch (Exception ignored) {
            purchaseResponseModel.setResponseCode(500);
            purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return purchaseResponseModel;
    }

    public PurchaseResponseModel initiateOrder(Response<PurchaseResponseModel> response) {
        PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
        try {

            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = 4008;//errorObject.getInt("responseCode");
                if (code == 4423) {
                    purchaseResponseModel.setResponseCode(4423);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.order_not_found));
                } else if (code == 4008) {
                    purchaseResponseModel.setResponseCode(4008);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.order_completed_or_failed));
                } else if (code == 4009) {
                    purchaseResponseModel.setResponseCode(4009);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.payment_provider_not_Supported));
                } else if (code == 4010) {
                    purchaseResponseModel.setResponseCode(4010);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.no_config_found));
                } else if (code == 4302) {
                    purchaseResponseModel.setResponseCode(4302);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 500) {
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }else {
                    purchaseResponseModel.setResponseCode(4405);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }

        } catch (Exception ignored) {
            purchaseResponseModel.setResponseCode(500);
            purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return purchaseResponseModel;
    }

    public PurchaseResponseModel updateOrder(Response<PurchaseResponseModel> response) {
        PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
        try {

            JSONObject errorObject = new JSONObject(response.errorBody().string());
            if (errorObject.getInt("responseCode") != 0) {
                int code = errorObject.getInt("responseCode");
                if (code == 4423) {
                    purchaseResponseModel.setResponseCode(4423);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.order_not_found));
                } else if (code == 4008) {
                    purchaseResponseModel.setResponseCode(4008);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.order_completed_or_failed));
                } else if (code == 4424) {
                    purchaseResponseModel.setResponseCode(4424);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.payment_id_not_found));
                } else if (code == 4011) {
                    purchaseResponseModel.setResponseCode(4011);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.purchase_token_required));
                } else if (code == 4013) {
                    purchaseResponseModel.setResponseCode(4013);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.payment_validation_failed));
                } else if (code == 4302) {
                    purchaseResponseModel.setResponseCode(4302);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.you_are_logged_out));
                } else if (code == 4012) {
                    purchaseResponseModel.setResponseCode(4012);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.receipt_validation_url_not_avail));
                } else if (code == 4010) {
                    purchaseResponseModel.setResponseCode(4010);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.no_config_found));
                } else if (code == 4009) {
                    purchaseResponseModel.setResponseCode(4009);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.payment_provider_not_Supported));
                }else {
                    purchaseResponseModel.setResponseCode(4405);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                }
            }

        } catch (Exception ignored) {
            purchaseResponseModel.setResponseCode(500);
            purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
        }
        return purchaseResponseModel;
    }
}
