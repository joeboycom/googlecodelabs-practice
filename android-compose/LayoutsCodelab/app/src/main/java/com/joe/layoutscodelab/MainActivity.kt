package com.joe.layoutscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.joe.layoutscodelab.ui.theme.LayoutsCodelabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsCodelabTheme {
                LayoutsCodelab()
            }
        }
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    LayoutsCodelabTheme {
        LayoutsCodelab()
    }
}

@Composable
fun LayoutsCodelab() {
    // Scaffold allows you to implement a UI with the basic Material Design layout structure.
    // It provides slots for the most common top-level Material components such as TopAppBar, BottomAppBar, FloatingActionButton and Drawer.
    // With Scaffold, you make sure these components will be positioned and work together correctly.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                // The slot for action items in the top AppBar is the actions parameter that internally uses a Row,
                // so multiple actions will be placed horizontally. In order to use one of the predefined icons, we can use the IconButton composable with an Icon inside it
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) {
        // All parameters in the Scaffold API are optional except the body content that is of
        // type @Composable (InnerPadding) -> Unit: the lambda receives a padding as a parameter.
        // That's the padding that should be applied to the content root composable to constrain the items appropriately on the screen
            innerPadding ->
        // Apply the modifier when calling the composable that will add the extra padding just when needed
        BodyContent(Modifier.padding(innerPadding).padding(8.dp))

    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    // Apply the modifier to the only direct child inside the composable so that all calls to BodyContent apply the extra padding
    Column(modifier = modifier.padding(8.dp)) {
//        Text(text = "Hi there!")
//        Text(text = "Thanks for going through the Layouts codelab")
//        ScrollingList()
//        MyOwnColumn(modifier.padding(8.dp)) {
//            Text("MyOwnColumn")
//            Text("places items")
//            Text("vertically.")
//            Text("We've done it by hand!")
//        }

        // Even though adding rows to five, there are still some items can not be seen.
        // As depending on the number of rows, our topics can go off the screen
//        StaggeredGrid(modifier = modifier, rows = 5) {
//            for (topic in topics) {
//                Chip(modifier = Modifier.padding(8.dp), text = topic)
//            }
//        }

        // we can make our BodyContent scrollable by just wrapping the StaggeredGrid in a scrollable Row and passing the modifier to it instead of StaggeredGrid.
        Row(
            // First, modifiers will update the constraints from left to right, and then, they return back the size from right to left. Let's see this better with an example:
            // Because the constraints are propagated through the chain from left to right,
            // the constraints with which the content of the Row to be measured are (200-16-16)=168 dp for both minimum and maximum width and height.
            // This means that the size of the StaggeredGrid will be exactly 168x168 dp.
            // Therefore, the final size of the scrollable Row, after the modifySize chain is run from right to left, will be 200x200 dp.
//            modifier = modifier
//                .background(color = Color.LightGray)
//                .size(200.dp)
//                .padding(16.dp)
//                .horizontalScroll(rememberScrollState())

            // In this case, the constraints that the scrollable Row and padding had originally will be coerced to the size constraints to measure the children.
            // Therefore, the StaggeredGrid will be constrained to 200 dp for both minimum and maximum width and height.
            // The StaggeredGrid size is 200x200 dp and as the size is modified from right to left,
            // the padding modifier will increment the size to (200+16+16)x(200+16+16)=232x232 that will be the final size of the Row as well.
            modifier = modifier
                .background(color = Color.LightGray, shape = RectangleShape)
                .padding(16.dp)
                .size(200.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            StaggeredGrid {
                for (topic in topics) {
                    Chip(modifier = Modifier.padding(8.dp), text = topic)
                }
            }
        }
    }
}

@Composable
fun ScrollingList() {
    val listSize = 100
    // We save the scrolling position with this state
    val scrollState = rememberLazyListState()
    // We save the coroutine scope where our animated scroll will be executed
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    // 0 is the first item index
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("Scroll to the top")
            }

            Button(onClick = {
                coroutineScope.launch {
                    // listSize - 1 is the last index of the list
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("Scroll to the end")
            }
        }

        // We save the scrolling position with this state that can also
        // be used to programmatically scroll the list
        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(it)
            }
        }
    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    // add some outer spacing, change the background color of the composable, and round the corners of the Row
    Row(modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colors.surface)
        .clickable(onClick = { /* Ignoring onClick */ })
        .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            // Image goes here
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            // LocalContentAlpha is defining opacity level of its children
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
    LayoutsCodelabTheme {
        PhotographerCard()
    }
}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutsCodelabTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutsCodelabTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }

    }
)

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

/* Using the Layout composable */
// Instead of controlling how a single composable gets measured and laid out on the screen,
// you might have the same necessity for a group of composables.
// For that, you can use the Layout composable to manually control how to measure and position the layout's children.
//@Composable
//fun CustomLayout(
//    modifier: Modifier = Modifier,
//    // custom layout attributes
//    content: @Composable () -> Unit
//) {
//    Layout(
//        modifier = modifier,
//        content = content
//    ) { measurables, constraints ->
//        // measure and position children given constraints logic here
//    }
//}

// If we wanted to make the grid reusable on different orientations, we could take as a parameter the number of rows we want to have on the screen.
// Remember you can only measure your children once.
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // 1.
        // For our use case, we won't constrain our child views further.
        // When measuring our children, we should also keep track of what's the width and the max height of each row

        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        // 2.
        // Now that we have the list of measured children in our logic, before positioning them on the screen, we need to calculate the size of our grid (full width and height).
        // Also, since we already know the maximum height of each row, we can calculate where we'll position the elements for each row in the Y position.
        // We save the Y positions in the rowY variable

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        // 3.
        // Lastly, we position our children on the screen by calling placeable.placeRelative(x, y).
        // In our use case, we also keep track of the X coordinate for each row in the rowX variable

        // Set the size of the parent layout
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    LayoutsCodelabTheme {
        Chip(text = "Hi there")
    }
}