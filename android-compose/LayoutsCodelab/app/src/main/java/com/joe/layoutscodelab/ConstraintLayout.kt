import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.compose.ui.layout.layoutId
import com.joe.layoutscodelab.ui.theme.LayoutsCodelabTheme

// Decoupled API
// So far, in the examples, constraints have been specified inline, with a modifier in the composable they're applied to.
// However, there are cases when keeping the constraints decoupled from the layouts they apply to is valuable:
// the common example is for easily changing the constraints based on the screen configuration or animating between 2 constraint sets.
//
// For these cases, you can use ConstraintLayout in a different way:
// 1. Pass in a ConstraintSet as a parameter to ConstraintLayout.
// 2. Assign references created in the ConstraintSet to composables using the layoutId modifier.
// This API shape applied to the first ConstraintLayout example shown above, optimized for the width of the screen, looks like this:
@Preview
@Composable
fun DecoupledConstraintLayoutPreview() {
    LayoutsCodelabTheme {
        DecoupledConstraintLayout()
    }
}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin= margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

@Composable
fun LargeConstraintLayout() {
    ConstraintLayout {
        val text = createRef()

//        val guideline = createGuidelineFromStart(fraction = 0.5f)
//        Text(
//            "This is a very very very very very very very long text",
//            Modifier.constrainAs(text) {
//                linkTo(start = guideline, end = parent.end)
//            }
//        )

        val guideline = createGuidelineFromStart(0.5f)
        Text(
            "This is a very very very very very very very long text",
            Modifier.constrainAs(text) {
                linkTo(guideline, parent.end)
                width = Dimension.preferredWrapContent
            }
        )

//        Available Dimension behaviors are:
//        preferredWrapContent  - the layout is wrap content, subject to the constraints in that dimension.
//        wrapContent           - the layout is wrap content even if the constraints would not allow it.
//        fillToConstraints     - the layout will expand to fill the space defined by its constraints in that dimension.
//        preferredValue        - the layout is a fixed dp value, subject to the constraints in that dimension.
//        value                 - the layout is a fixed dp value, regardless of the constraints in that dimension
//
//        Also, certain Dimensions can be coerced:
//        width = Dimension.preferredWrapContent.atLeast(100.dp)
    }
}

@Preview
@Composable
fun LargeConstraintLayoutPreview() {
    LayoutsCodelabTheme {
        LargeConstraintLayout()
    }
}

// Note: In the View system, ConstraintLayout was the recommended way to create large and complex layouts as the flat view hierarchy was better for performance.
// However, this is not a concern in Compose, which is able to efficiently handle deep layout hierarchies.
// Note that:
// - barriers (and all the other helpers) can be created in the body of ConstraintLayout, but not inside constrainAs.
// - linkTo can be used to constrain with guidelines and barriers the same way it works for edges of layouts
@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {

        // References are created using createRefs() (or createRef()) and each composable in ConstraintLayout needs to have a reference associated.
        val (button1, button2, text1, text2) = createRefs()

        Button(
            onClick = { /* Do something */ },
            // Assign reference "button" to the Button composable
            // and constrain it to the top of the ConstraintLayout
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
                absoluteLeft.linkTo(parent.absoluteLeft, margin = 16.dp)
            }
        ) {
            Text("Button")
        }

        // Assign reference "text" to the Text composable
        // and constrain it to the bottom of the Button composable
        Text("Text1", Modifier.constrainAs(text1) {
            top.linkTo(button1.bottom, margin = 16.dp)
        })

        Text("Text2", Modifier.constrainAs(text2) {
            top.linkTo(text1.bottom, margin = 16.dp)
            // Centers Text horizontally in the ConstraintLayout
            centerHorizontallyTo(parent)
        })

        val barrier = createEndBarrier(button1, text2)
        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button 2")
        }
    }
}

@Preview
@Composable
fun ConstraintLayoutContentPreview() {
    LayoutsCodelabTheme {
        ConstraintLayoutContent()
    }
}
