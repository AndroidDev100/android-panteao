package panteao.make.ready.beanModel.responseModels

data class MyPurchasesResponseModel(
    var data: Data = Data(),
    var debugMessage: String = "",
    var responseCode: Int = 0,
    var isSuccessful: Boolean = true
)

data class Data(
    var items: List<Item> = emptyList(),
    var pageNumber: Int = 0,
    var pageSize: Int = 0,
    var totalElements: Int = 0,
    var totalPages: Int = 0
)

data class Item(
    var contentSKU: String = "",
    var createdDate: String = "",
    var id: String = "",
    var offerIdentifier: String = "",
    var offerTitle: String = "",
    var orderAmount: String = "",
    var orderCurrency: String = "",
    var orderId: String = "",
    var orderStatus: String = "",
    var paymentId: String = "",
    var paymentProvider: String = "",
    var renewalDate: String = "",
    var rentalExpiryDate: String = "",
    var subscriptionExpiryDate: String = "",
    var subscriptionOfferType: String = "",
    var userId: String = "",
    var voDOfferType: String = ""
)