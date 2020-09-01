<main class="col-md markdown-body">

<h1 id="pokédex">Pokédex</h1>
<h2 id="distribution-code">Distribution Code</h2>
<p>Download this project’s <a href="https://cdn.cs50.net/2019/fall/tracks/android/pokedex/pokedex.zip">distribution code</a>.</p>

<p>To open the distribution code, extract the ZIP, open Android Studio, select “Import project”, and select the folder you extracted from the ZIP.</p>

<h2 id="what-to-do">What To Do</h2>

<ul>
  <li data-marker="*">Searching</li>
  <li data-marker="*">Catching</li>
  <li data-marker="*">Saving State</li>
  <li data-marker="*">Sprites</li>
  <li data-marker="*">Description</li>
</ul>

<h2 id="searching">Searching</h2>

<p>Let’s add some new functionality to our Pokédex app! First, let’s give users the ability to search the Pokédex for their favorite Pokémon.</p>

<p>To start, we’re going to use a built-in feature of <code class="highlighter-rouge">Adapter</code> called <code class="highlighter-rouge">Filterable</code>. This interface allows us to apply a filter to the data stored in our <code class="highlighter-rouge">Adapter</code> , which is exactly what we need! We’ll filter out any Pokémon whose names don’t match the search text.</p>

<p>First, make sure that the <code class="highlighter-rouge">adapter</code> variable in <code class="highlighter-rouge">MainActivity</code> has the type <code class="highlighter-rouge">PokedexAdapter</code>, like this:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>private PokedexAdapter adapter;
</code></pre></div></div>

<p>We’ll be calling methods that are specific to our <code class="highlighter-rouge">PokedexAdapter</code> that don’t exist on the base <code class="highlighter-rouge">Adapter</code> class, so we need to use the <code class="highlighter-rouge">PokedexAdapter</code> type.</p>

<p>Next, open up the <code class="highlighter-rouge">PokedexAdapter</code> class. We can specify that our <code class="highlighter-rouge">PokedexAdapter</code> implements <code class="highlighter-rouge">Filterable</code> by changing the class declaration to:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>public class PokedexAdapter extends RecyclerView.Adapter&lt;PokedexAdapter.PokedexViewHolder&gt; implements Filterable {
</code></pre></div></div>

<p>Recall that an interface is just a list of methods that any class can implement. Now that we’ve implemented <code class="highlighter-rouge">Filterable</code>, we can add a new method called <code class="highlighter-rouge">getFilter</code> to the <code class="highlighter-rouge">PokedexAdapter</code>.</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>@Override
public Filter getFilter() {
  return new PokemonFilter();
}
</code></pre></div></div>

<p>Of course, we don’t have a class called <code class="highlighter-rouge">PokemonFilter</code> yet, so let’s create one! We can create this class inside of <code class="highlighter-rouge">PokedexAdapter</code>, just as we did with <code class="highlighter-rouge">PokedexViewHolder</code>, like this:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>private class PokemonFilter extends Filter {
  @Override
  protected FilterResults performFiltering(CharSequence constraint) {
    // implement your search here!
  }

  @Override
  protected void publishResults(CharSequence constraint, FilterResults results) {
  }
}
</code></pre></div></div>

<p>You can implement your search inside <code class="highlighter-rouge">performFiltering</code>. The argument to this method, <code class="highlighter-rouge">constraint</code>, will be whatever text the user has typed into the search bar, which you can use for your filter. The <code class="highlighter-rouge">performFiltering</code> method should return an instance of <code class="highlighter-rouge">FilterResults</code>. Here’s an example:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>@Override
protected FilterResults performFiltering(CharSequence constraint) {
  // implement your search here!
  FilterResults results = new FilterResults();
  results.values = filteredPokemon; // you need to create this variable!
  results.count = filteredPokemon.size();
  return results
}
</code></pre></div></div>

<p>The instance of <code class="highlighter-rouge">FilterResults</code> that you return from <code class="highlighter-rouge">performFiltering</code> will then be passed to <code class="highlighter-rouge">publishResults</code>. Inside of <code class="highlighter-rouge">publishResults</code>, you probably want to store the results of the search in another class variable, so you don’t lose your copy of the list containing all Pokémon (i.e., the <code class="highlighter-rouge">pokemon</code> variable). Assuming you call this variable <code class="highlighter-rouge">List&lt;Pokemon&gt; filtered</code>, then your implementation of <code class="highlighter-rouge">publishResults</code> might look like this:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>@Override
protected void publishResults(CharSequence constraint, FilterResults results) {
  filtered = (List&lt;Pokemon&gt;) results.values;
  notifyDataSetChanged();
}
</code></pre></div></div>

<p>Then, rather than using the <code class="highlighter-rouge">pokemon</code> variable inside of methods like <code class="highlighter-rouge">onBindViewHolder</code> and <code class="highlighter-rouge">getItemCount</code>, use your new <code class="highlighter-rouge">filtered</code> variable.</p>

<p>Now that the filtering logic is done, let’s add a search bar above our <code class="highlighter-rouge">RecyclerView</code>. On the left-hand side of Android Studio, expand the <code class="highlighter-rouge">app</code> folder, and you should see a folder called <code class="highlighter-rouge">res</code>. Recall that this is where the XML files for our layouts are stored. Right click on <code class="highlighter-rouge">res</code>, then select New &gt; Android Resource Directory. Enter <code class="highlighter-rouge">menu</code> for both <code class="highlighter-rouge">Directory name</code> and <code class="highlighter-rouge">Resource type</code>, then press <code class="highlighter-rouge">OK</code>. You should now see a new directory called <code class="highlighter-rouge">menu</code> underneath <code class="highlighter-rouge">res</code>.</p>

<p>Next, right click on that <code class="highlighter-rouge">menu</code> directory and select New &gt; Menu resource file. Call this file <code class="highlighter-rouge">main_menu.xml</code> and then click <code class="highlighter-rouge">OK</code>. This new XML file will contain the layout for our menu. Paste the below into that file:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code><span class="cp">&lt;?xml version="1.0" encoding="utf-8"?&gt;</span>
<span class="nt">&lt;menu</span> <span class="na">xmlns:android=</span><span class="s">"http://schemas.android.com/apk/res/android"</span>
    <span class="na">xmlns:app=</span><span class="s">"http://schemas.android.com/apk/res-auto"</span><span class="nt">&gt;</span>

    <span class="nt">&lt;item</span> <span class="na">android:id=</span><span class="s">"@+id/action_search"</span>
        <span class="na">android:title=</span><span class="s">"Search"</span>
        <span class="na">app:actionViewClass=</span><span class="s">"androidx.appcompat.widget.SearchView"</span>
        <span class="na">app:showAsAction=</span><span class="s">"always"</span> <span class="nt">/&gt;</span>
<span class="nt">&lt;/menu&gt;</span>
</code></pre></div></div>

<p>As you can see, we’re creating a new <code class="highlighter-rouge">menu</code> element with one <code class="highlighter-rouge">item</code> child. The <code class="highlighter-rouge">item</code> represents a search icon, that when pressed, will open up a <code class="highlighter-rouge">SearchView</code>.</p>

<p>Now, we can wire up that <code class="highlighter-rouge">SearchView</code> to our <code class="highlighter-rouge">MainActivity</code>. First, we need to make <code class="highlighter-rouge">MainActivity</code> implement an interface called <code class="highlighter-rouge">SearchView.OnQueryTextListener</code>. To tell Android that our main activity class implements <code class="highlighter-rouge">SearchView.OnQueryTextListener</code>, change the declaration of the class to the below:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
</code></pre></div></div>

<p>Next, to use the layout file we just created, we need to implement a method on our <code class="highlighter-rouge">MainActivity</code> called <code class="highlighter-rouge">onCreateOptionsMenu</code>.</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(this);

    return true;
}
</code></pre></div></div>

<p>As you’d guess, this method is called when an activity is creating a menu. Let’s walk through this code line-by-line. First, we’re specifying that this activity should use <code class="highlighter-rouge">R.menu.main_menu</code>, which is the name of the XML file we created. Then, we’re grabbing a reference to the <code class="highlighter-rouge">item</code> inside our menu using its ID, <code class="highlighter-rouge">action_search</code>. Finally, we’re calling <code class="highlighter-rouge">setOnQueryTextListener</code> on the <code class="highlighter-rouge">SearchView</code> in order to specify that our search code will be specified in our <code class="highlighter-rouge">MainActivity</code> class (which is what <code class="highlighter-rouge">this</code> references).</p>

<p>Now, our <code class="highlighter-rouge">SearchView</code> will automatically call methods on <code class="highlighter-rouge">MainActivity</code> when the user types text into the <code class="highlighter-rouge">SearchView</code>. Specifically, a method called <code class="highlighter-rouge">onQueryTextChange</code> will be called, and the argument passed to that method will be a <code class="highlighter-rouge">String</code> representing the current text of the <code class="highlighter-rouge">SearchView</code>. We then want to pass that along to the <code class="highlighter-rouge">PokemonFilter</code> we created earlier, like this, so our UI will update:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>@Override
public boolean onQueryTextChange(String newText) {
    adapter.getFilter().filter(newText);
    return false;
}
</code></pre></div></div>

<p>Along the same line, a method called <code class="highlighter-rouge">onQueryTextSubmit</code> will be called when the user presses the “submit” button on the keyboard, which you can handle in the same way:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>@Override
public boolean onQueryTextSubmit(String newText) {
    adapter.getFilter().filter(newText);
    return false;
}
</code></pre></div></div>

<p>At this point, everything should be wired up, so you can test out your new search functionality!</p>

<h2 id="catching">Catching</h2>

<p>Any good Pokédex keeps track of which Pokémon have been caught and which haven’t. Let’s add that functionality to our Pokédex as well.</p>

<p>First, let’s add a new <code class="highlighter-rouge">Button</code> to the <code class="highlighter-rouge">PokemonActivity</code>. Open up the layout XML file, and then add a new <code class="highlighter-rouge">&lt;Button&gt;</code> element. You can set the text of this button to whatever you’d like, but we’ll go with <code class="highlighter-rouge">Catch</code> for simplicity.</p>

<p>To handle taps on the <code class="highlighter-rouge">Button</code>, we can use the attribute <code class="highlighter-rouge">android:onClick="toggleCatch"</code>. Add that to your <code class="highlighter-rouge">Button</code>, and then a method called <code class="highlighter-rouge">public void toggleCatch(View view)</code> will automatically be called whenever the user presses on the button.</p>

<p>Naturally, you’ll want to add that method to your <code class="highlighter-rouge">PokemonActivity</code>, like this:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>public void toggleCatch(View view) {
  // gotta catch 'em all!
}
</code></pre></div></div>

<p>Now, we can implement catching. To start, add a new boolean class variable that keeps track of whether or not the Pokémon is caught. If a Pokémon is caught, change the text of the button to something like <code class="highlighter-rouge">Release</code>, and vice-versa when it’s released. The <code class="highlighter-rouge">Button</code> method <code class="highlighter-rouge">setText(String text)</code> method will come in handy.</p>

<h2 id="saving-state">Saving State</h2>

<p>You’ll notice that if you stop running your app and then run it again, your Pokédex will forget which Pokémon are caught and which aren’t! Let’s fix that by saving that state to disk.</p>

<p>As your last task, use the <code class="highlighter-rouge">SharedPreferences</code> class to save which Pokémon are caught. With this class, you can store state that will be remembered each time your app launches, which is just what you need. How you store this state is up to you—you might consider storing a list of all Pokémon that are caught, or you might consider using a map from Pokémon to boolean values.</p>

<p>Here’s an example:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>getPreferences(Context.MODE_PRIVATE).edit().putString("course", "cs50").commit();
String course = getPreferences(Context.MODE_PRIVATE).getString("course", "cs50");
// course is equal to "cs50"
</code></pre></div></div>

<p>To test saving state, you should be able to catch a Pokémon, stop the simulator, start the simulator again, and still see that Pokémon as caught.</p>

<h2 id="sprites">Sprites</h2>

<p>Every Pokémon aficionado has noticed by now that our Pokédex doesn’t yet have arguably its most important feature: the ability to display what each Pokémon looks like! Luckily for us, the API we chose contains links to images for each Pokémon.</p>

<p>Let’s add that functionality to our app. First, add a new <code class="highlighter-rouge">ImageView</code> to the layout for <code class="highlighter-rouge">PokemonActivity</code>. Give it a unique ID, and then create an <code class="highlighter-rouge">ImageView</code> class variable inside of <code class="highlighter-rouge">PokemonActivity</code>, and use <code class="highlighter-rouge">findViewById</code> to map that variable to your layout.</p>

<p>Next, when parsing the response from the API call, take a look at the key called <code class="highlighter-rouge">sprites</code>. You’ll notice that it’s a dictionary, and the key <code class="highlighter-rouge">front_default</code> contains a URL pointing to an image of a Pokémon. Use the value of that key to load in an image to your <code class="highlighter-rouge">ImageView</code>. You’ll want to follow a similar pattern as before—use methods like <code class="highlighter-rouge">getJSONObject</code> and <code class="highlighter-rouge">getString</code> to parse the JSON strings into Java objects.</p>

<p>Once you have the URL of the image, you’ll need to download it from the Internet. To do so, we’ll use an Android built-in called <code class="highlighter-rouge">AsyncTask</code>. An <code class="highlighter-rouge">AsyncTask</code> executes some code in the background, so your app doesn’t lock up as the image is downloading. To use an <code class="highlighter-rouge">AsyncTask</code>, create a new class that looks like this:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>private class DownloadSpriteTask extends AsyncTask&lt;String, Void, Bitmap&gt; {
  @Override
  protected Bitmap doInBackground(String... strings) {
    try {
      URL url = new URL(strings[0]);
      return BitmapFactory.decodeStream(url.openStream());
    }
    catch (IOException e) {
      Log.e("cs50", "Download sprite error", e);
      return null;
    }
  }

  @Override
  protected void onPostExecute(Bitmap bitmap) {
    // load the bitmap into the ImageView!
  }
}
</code></pre></div></div>

<p>Let’s walk through this. On the first line, we’re specifying that our <code class="highlighter-rouge">AsyncTask</code> takes a <code class="highlighter-rouge">String</code> as input, and will return a <code class="highlighter-rouge">Bitmap</code>. That makes sense, since we’ll be passing in a URL as a <code class="highlighter-rouge">String</code>, and we expect a <code class="highlighter-rouge">Bitmap</code> object, which represents an image, in exchange. The <code class="highlighter-rouge">doInBackground</code> method is where we’ll put the logic to actually download an image. You’ll notice that this method actually takes an array of strings, but we only need to download one, so we’re just taking the first element in that array with <code class="highlighter-rouge">strings[0]</code>.</p>

<p>After <code class="highlighter-rouge">doInBackground</code> completes, the method called <code class="highlighter-rouge">onPostExecute</code> will be called. The <code class="highlighter-rouge">Bitmap</code> argument that’s passed in represents a loaded image, so load that into your <code class="highlighter-rouge">ImageView</code> using the method <code class="highlighter-rouge">setImageBitmap</code>.</p>

<p>Finally, you can use this new class to trigger a download of a string URL with:</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>new DownloadSpriteTask().execute(url); // you need to get the url!
</code></pre></div></div>

<p>You can test your code by selecting Pokémon from the list, and you should see images in the <code class="highlighter-rouge">ImageView</code>!</p>

<h2 id="description">Description</h2>

<p>Let’s add one last feature to our Pokédex: a description of each Pokémon. From the API documentation, we can see that we can use /api/v2/pokemon-species/{id} to retrieve a description for a given Pokémon: <a href="https://pokeapi.co/docs/v2#pokemon-species">https://pokeapi.co/docs/v2#pokemon-species</a>. For instance, the URL <code class="highlighter-rouge">https://pokeapi.co/api/v2/pokemon-species/133/</code> will give you the description text for everyone’s favorite Pokémon.</p>

<p>Specifically, what we’re looking for can be found in the key called <code class="highlighter-rouge">flavor_text_entries</code>. This key happens to contain entries for several different languages, but we’re just concerned with English for now. You might need a few additional structs to model the data for these new keys.</p>

<p>After a user selects a Pokémon from the list, make a separate API call to this second endpoint to retrieve the description of the selected Pokémon. Filter for just the first English description, and then display it somewhere on the screen. (Some Pokémon have more than one English description, and it suffices to just display the first one.) You’ll probably want to wire up a new <code class="highlighter-rouge">TextView</code> to display this final piece of data.</p>

<p>You should see a few sentences about each Pokémon after selecting it from the list!</p>

<h2 id="how-to-submit">How to Submit</h2>

<p>To submit your code with <code class="highlighter-rouge">submit50</code>, you may either: (1) upload your code to CS50 IDE and run <code class="highlighter-rouge">submit50</code> from inside of your IDE, or (2) install <code class="highlighter-rouge">submit50</code> on your own computer by running <code class="highlighter-rouge">pip3 install submit50</code> (assuming you have <a href="https://www.python.org/downloads/">Python 3</a> installed).</p>

<p>Execute the below, logging in with your GitHub username and password when prompted. For security, you’ll see asterisks (<code class="highlighter-rouge">*</code>) instead of the actual characters in your password.</p>

<div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>submit50 cs50/problems/2020/x/tracks/android/pokedex
</code></pre></div></div>


                </main>
