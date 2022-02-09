package com.joe.layoutscodelab

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joe.layoutscodelab.ui.theme.LayoutsCodelabTheme

// Row's minIntrinsicHeight will be the maximum minIntrinsicHeight of its children.
// Divider's minIntrinsicHeight is 0 as it doesn't occupy space if no constraints are given;
// Text's minIntrinsicHeight will be that of the text given a specific width. Therefore, Row's height constraint will be the max minIntrinsicHeight of the Texts.
// Divider will then expand its height to the height constraint given by Row.
@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    LayoutsCodelabTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}


//DIY Plus
// Whenever you are creating your custom layout, you can modify how intrinsics are calculated with the (min|max)Intrinsic(Width|Height) of the MeasurePolicy interface;
// however, the defaults should be enough most of the time.
//
// Also, you can modify intrinsics with modifiers overriding the Density.
// (min|max)Intrinsic(Width|Height)Of methods of the Modifier interface which also have a good default.