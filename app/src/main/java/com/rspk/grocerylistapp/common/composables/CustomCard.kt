package com.rspk.grocerylistapp.common.composables

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.constants.integers_numbers_list
import com.rspk.grocerylistapp.constants.natural_numbers_list

@Composable
fun CustomHomePageItemDetailsCard(
    modifier: Modifier = Modifier,
    image:String,
    text:String,
    avgPrice:String,
    tagColor: Color,
    starRating:Float,
    configuration: Configuration = LocalConfiguration.current
){
    val price = if(avgPrice.length >= 9 && configuration.orientation == Configuration.ORIENTATION_PORTRAIT) avgPrice.substring(0,6).plus("...") else avgPrice
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .weight(1.35f)
                .background(colorResource(id = R.color.recommendations_main_card_color))
        ){
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = text,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.recommendations_smaller_card_image_background))
            )
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Surety",
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_10))
                    .size(dimensionResource(id = R.dimen.size_25)),
                tint = tagColor
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .background(colorResource(id = R.color.recommendations_main_card_color))
        ) {
            Text(
                text = stringResource(id = R.string.average_price, price),
                modifier = Modifier
                    .weight(0.22f)
                    .padding(dimensionResource(id = R.dimen.padding_5)),
                color = colorResource(id = R.color.recommendations_smaller_card_text_color)
            )
            Text(
                text = text,
                modifier = Modifier
                    .weight(0.21f)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_5))
                    .padding(bottom = dimensionResource(id = R.dimen.padding_5)),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.recommendations_smaller_card_text_color)
            )
            StarRatingIndicator(rating = starRating, modifier = Modifier.weight(0.21f))
        }
    }
}


@Composable
fun StarRatingIndicator(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 24.dp,
    tint: Color = colorResource(id = R.color.gold_color)
) {
    val fullStars = rating.toInt()
    val hasHalfStar = (rating % 1) >= 0.5
    val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

    Row(modifier = modifier) {
        repeat(fullStars) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "Full Star",
                tint = tint,
                modifier = Modifier.size(starSize)
            )
        }
        if (hasHalfStar) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_half_24),
                contentDescription = "Half Star",
                tint = tint,
                modifier = Modifier.size(starSize)
            )
        }
        repeat(emptyStars) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_border_24),
                contentDescription = "Empty Star",
                tint = Color.Gray,
                modifier = Modifier.size(starSize)
            )
        }
    }
}


@Composable
fun SearchResultsItemDetailCard(
    image:String,
    text:String,
    quantityType:String,
    avgPrice: String,
    rating:String,
    isExpanded:Boolean,
    isAlreadyPresentInCart:Boolean,
    dropdownMenuBoxItemsScope:Boolean,
    tagColor: Color,
    onClick:()->Unit ={},
    onExpandedChanged:(Boolean)->Unit,
    onTextFieldValueChanged:(String)->Unit,
    textFiledComposable:@Composable (Modifier) -> Unit
){
    val price = if(avgPrice.substringBefore(" ").length >= 9)
        avgPrice.substringBefore(" ").plus("...") else avgPrice

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_240))
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_4),
                horizontal = dimensionResource(id = R.dimen.padding_8)
            )
            .border(
                width = 0.3.dp,
                color = colorResource(id = R.color.grocery_card_border),
                shape = RoundedCornerShape(5.dp)
            ),
    ) {
        Row {
            BoxWthImageAndTopIcon(
                image = image,
                text = text,
                tagColor = tagColor
            )
            Column(
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_10)),
            ) {
                GrocerCardDetails(
                    text = text,
                    price = price,
                    rating = rating,
                    quantityType = quantityType,
                    isExpanded = isExpanded,
                    onExpandedChanged = onExpandedChanged,
                    onTextFieldValueChanged = onTextFieldValueChanged,
                    textFiledComposable = textFiledComposable,
                    isAlreadyPresentInCart = isAlreadyPresentInCart,
                    dropdownMenuBoxItemsScope = dropdownMenuBoxItemsScope,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun RowScope.BoxWthImageAndTopIcon(
    image:String,
    text:String,
    tagColor: Color
){
    Box(modifier = Modifier.weight(0.40f)){
        Image(
            painter = rememberAsyncImagePainter(model = image) ,
            contentDescription = text,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                .background(colorResource(id = R.color.grocery_image_background))
        )
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Surety",
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_10))
                .size(dimensionResource(id = R.dimen.size_25)),
            tint = tagColor
        )
    }
}

@Composable
fun ColumnScope.GrocerCardDetails(
    text:String,
    price:String,
    rating:String,
    quantityType:String,
    isExpanded:Boolean,
    onExpandedChanged:(Boolean)->Unit,
    onTextFieldValueChanged:(String)->Unit,
    textFiledComposable:@Composable (Modifier) -> Unit,
    isAlreadyPresentInCart:Boolean,
    dropdownMenuBoxItemsScope:Boolean,
    onClick:()->Unit
){
    GroceryCardCustomText(
        text = text,
        fontWeight = FontWeight.SemiBold
    )
    GroceryCardCustomText(
        text = stringResource(id = R.string.average_price,price),
        fontSize = 15.sp
    )
    StarRatingIndicator(rating = rating.toFloatOrNull() ?: 0f, modifier = Modifier.weight(0.1f))
    CustomDropDownMenu(
        isExpanded = isExpanded,
        onExpandedChanged = onExpandedChanged,
        textFieldComposable = textFiledComposable,
        onTextFieldValueChange = onTextFieldValueChanged,
        items = if(dropdownMenuBoxItemsScope) integers_numbers_list else natural_numbers_list,
        modifier = Modifier.weight(0.13f)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.09f)
            .padding(
                start = dimensionResource(id = R.dimen.padding_5),
                bottom = dimensionResource(id = R.dimen.padding_5)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_20))
    ){
        Text(
            text = stringResource(id = R.string.quantity_type, quantityType),
            fontSize = 10.sp,
            color = colorResource(id = R.color.grocery_text_color)
        )
        if (isAlreadyPresentInCart){
            Text(
                text = stringResource(id = R.string.already_added_to_cart),
                fontSize = 10.sp,
                color = colorResource(id = R.color.grocery_text_color)
            )
        }
    }
    Button(onClick = {
        onClick()
    }, modifier = Modifier
        .fillMaxWidth()
        .weight(0.1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.grocery_button_background),
            contentColor = colorResource(id = R.color.grocery_button_text_color)
        )
    ) {
        Text(text = "Add to Cart")
    }
}

@Composable
fun UsersCartCard(
    image:String,
    title:String,
    description:String,
    avgPrice: String,
    rating: String,
    isChecked:Boolean,
    quantityType: String,
    onCheckedChange:(Boolean)->Unit,
    quantity:String,
    onQuantityChanged:(String)->Unit,
    decreaseAmountBasedOnItem:Boolean,
    isItemNeedToUpdate:Boolean,
    onDeleteClick:() -> Unit = {},
    onUpdateClick:() -> Unit = {},
    configuration: Configuration = LocalConfiguration.current
){
    val portraitConfiguration = remember { configuration.orientation == Configuration.ORIENTATION_PORTRAIT }
    val smallDescription = remember { if(description.length > 60 && portraitConfiguration) description.substring(0,60).plus("...") else description }
    val price = remember {
        if(avgPrice.substringBefore(" ").length >= 10 && portraitConfiguration)
            avgPrice.substring(0,11).plus("...")
        else avgPrice
    }

        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_10))
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(colorResource(R.color.cart_card_background_color))
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_10),
                    vertical = dimensionResource(id = R.dimen.padding_20)
                )
        ){
            Column (
                modifier = Modifier
                    .weight(0.40f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.TopStart),
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.cart_checkbox_color)
                        )
                    )
                    Image(
                        painter = rememberAsyncImagePainter(image) ,
                        contentDescription = "Cart",
                    )
                }
                CustomTextFieldWithButtons(
                    quantity = quantity,
                    onQuantityChanged = onQuantityChanged,
                    modifier = Modifier
                        .weight(0.35f),
                    decreaseAmountBasedOnItem = decreaseAmountBasedOnItem,
                    onDeleteClicked = {
                        onDeleteClick()
                    }
                )
                Text(
                    text = stringResource(id = R.string.quantity_type, quantityType),
                    modifier = Modifier,
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.cart_pill_text_color)
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxSize()
                    .padding(start = 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${title.trim()}\n$smallDescription",
                    modifier = Modifier
                        .weight(0.35f)
                        .padding(5.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.cart_pill_text_color)
                )
                Text(
                    text = stringResource(id = R.string.average_price, price),
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(dimensionResource(id = R.dimen.padding_5)),
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.cart_pill_text_color)
                )
                StarRatingIndicator(rating = rating.toFloat(), modifier = Modifier.weight(0.1f))
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.2f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_7))
                ){
                    Button(
                        onClick = {
                            onDeleteClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                color = colorResource(id = R.color.cart_button_outlined_border_color),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .weight(0.45f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "Delete",
                            color =  colorResource(id = R.color.cart_button_outlined_text_color)
                        )
                    }
                    if(isItemNeedToUpdate){
                        Button(
                            onClick = {
                                onUpdateClick()
                            },
                            modifier = Modifier
                                .weight(0.45f)
                                .clip(RoundedCornerShape(50.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.cart_button_filled_background),
                            )
                        ) {
                            Text(
                                text = "Update",
                                color = colorResource(id = R.color.cart_button_filled_text_color),
                                modifier = Modifier.padding(vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
}


@Composable
fun UserUploadedBillsCard(
    image: Uri,
    fileName:String,
    dateUploaded:String,
    type:String,
    fileSize:String,
    menuItems:Map<String,() -> Unit>
){
    val name = fileName.substring(0,18).plus("...")
    var isExpanded by remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_10))
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_350))
            .clip(RoundedCornerShape(9.dp))
            .background(colorResource(id = R.color.image_card_background_color))
    ) {
        Image(
            painter = rememberAsyncImagePainter(image) ,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_100))
                .background(colorResource(id = R.color.image_card_background_color))
                .padding(dimensionResource(id = R.dimen.padding_10)),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.weight(0.8f)
                ) {
                    Text(
                        text = stringResource(id = R.string.file_name, name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.image_card_main_text_color)
                    )
                    Text(
                        text = stringResource(id = R.string.date_uploaded, dateUploaded),
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.image_card_medium_text_color)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Text(
                            text = stringResource(id = R.string.file_size, fileSize),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.image_card_small_text_color)
                        )
                        Text(
                            text = stringResource(id = R.string.file_type, type),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.image_card_small_text_color)
                        )
                    }
                }
                CustomOptionMenu(
                    isExpanded = isExpanded,
                    onExpandedChanged = {isExpanded = it},
                    items = menuItems,
                    modifier = Modifier.weight(0.2f)
                )
            }
        }
    }
}
