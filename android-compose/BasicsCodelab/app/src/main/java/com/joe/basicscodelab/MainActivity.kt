package com.joe.basicscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joe.basicscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {

    // add the logic to show the different screens in MyApp, and hoist the state.
    // shouldShowOnboarding is using a by keyword instead of the =. This is a property delegate that saves you from typing .value every time.
    // var shouldShowOnboarding by remember { mutableStateOf(true) }

    // The remember function works only as long as the composable is kept in the Composition. When you rotate, the whole activity is restarted so all state is lost.
    // This also happens with any configuration change and on process death.
    // Instead of using remember you can use rememberSaveable. This will save each state surviving configuration changes (such as rotations) and process death.
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    // In Compose you don't hide UI elements. Instead, you simply don't add them to the composition,
    // so they're not added to the UI tree that Compose generates.
    // You do this with simple conditional Kotlin logic.
    // For example to show the onboarding screen or the list of greetings you would do something like:
    if (shouldShowOnboarding) {
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
    } else {
        Greetings()
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit) {

    Surface {
        // Column can be configured to display its contents in the center of the screen.
        // The modifier align can be used to align composables inside a row or a column.
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }
}

// You have added a new composable called OnboardingScreen and also a new preview.
// If you build the project you'll notice you can have multiple previews at the same time.
// We also added a fixed height to verify that the content is aligned correctly.
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" } ) {
    // LazyColumn and LazyRow are equivalent to RecyclerView in Android Views.
    // Make sure you import androidx.compose.foundation.lazy.items as Android Studio will pick a different items function by default.
    // LazyColumn doesn't recycle its children like RecyclerView. It emits new Composables as you scroll through it and is still performant, as emitting Composables is relatively cheap compared to instantiating Android Views.
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {

    // Note: If you expand item number 1, you scroll away to number 20 and come back to 1, you'll notice that 1 is back to the original size.
    // You could save this data with rememberSaveable if it were a requirement, but we are keeping the example simple.
    var expanded by rememberSaveable { mutableStateOf(false) }

    // simple animation which is just expanding the item
    // val extraPadding by animateDpAsState(if (expanded) 48.dp else 0.dp)

    // animateDpAsState takes an optional animationSpec parameter that lets you customize the animation.
    // The spring spec does not take any time-related parameters. Instead it relies on physical properties (damping and stiffness) to make animations more natural.

    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            // Note that we are also making sure that padding is never negative, otherwise it could crash the app. This introduces a subtle animation bug that we'll fix later in Finishing touches.
            Column(modifier = Modifier.weight(1f).padding(bottom = extraPadding.coerceAtLeast(0.dp))) {
            Text(text = "Hello, ")
                Text(text = name)
            }
            OutlinedButton(
                onClick = { expanded = !expanded }
            ) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

// set dimensions or added any constraints to the size of your composables
@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    BasicsCodelabTheme {
        Greetings()
    }
}