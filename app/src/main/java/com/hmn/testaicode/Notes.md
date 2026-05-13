1) What is “allocation of a NavController”?
   It means creating an instance of a NavController object in memory (in Compose you usually do it with rememberNavController()).
At this point it’s just an object that can navigate, but it may not yet know what routes exist.

2) “The graph is attached when a NavHost …” — what is “graph”?
   Graph = the navigation map (the set of destinations + actions) that this controller knows about.
   In Compose, the graph is the structure you define inside:

`NavHost(navController = navController, startDestination = "A") {
composable("A") { ... }
navigation(route = "MAIN_MENU_GRAPH", startDestination = "HOME") { ... }
}`

So yes: your navigation(route = Routes.MAIN_MENU_GRAPH, ...) { ... } is one subgraph inside the overall graph.

3) Sample code (not your project): one graph ↔ one controller

Correct: same controller that owns the graph is used to navigate.
`@Composable
fun App() {
val navController = rememberNavController()
NavHost(navController, startDestination = "home") {
composable("home") {
Button(onClick = { navController.navigate("settings") }) { Text("Go") }
}
composable("settings") { Text("Settings") }
}
}`

Wrong: new controller not attached to any NavHost graph.

`@Composable
fun HomeScreenWrong() {
val navController = rememberNavController() // new controller
Button(onClick = { navController.navigate("settings") }) { Text("Go") }
// Crash: this controller has no graph because no NavHost used it
}`

That’s the meaning of one graph ↔ one controller: the controller you call navigate() on must be the same controller that was given to NavHost(...) { ... } that declares the route.



* Difference / relation between NavHost, composable, navigation
  
* NavHost(navController, startDestination) { ... }
   The “container” that attaches a graph to a controller and shows the current destination’s UI.
  

* composable("route") { ... }
   A destination in the graph: “when route == X, render this Composable content”.

* navigation(route = "...", startDestination = "...") { ... }
   A nested graph (subgraph) used to group destinations under one route (useful for flows like auth/main/settings).
   Relation: NavHost defines the graph; inside it you add destinations via composable(...) and you can organize them into subgraphs via navigation(...).