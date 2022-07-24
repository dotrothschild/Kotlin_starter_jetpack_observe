# Kotlin_starter_jetpack_observe
Getting started framework using latest android best practices


New way to use sensors with live data
New Project: Fragment + ViewModel
My Sample Sensors With Live Data
Step 1: Update build.gradle.
Create a new project, select the template Fragment + ViewModel
Open build.grade and pause mouse over any highlighted dependencies, accept the recommended update. Add the following code just before end of android group. viewBinding is used, databinding is not used, but included for completeness.
…}
    buildFeatures {
        dataBinding true
        viewBinding true
    }
}

dependencies {…
 Upper left is text, Synch now, click to synch (this updates dependencies)
Step 2: Create a bottom nav menu
1.	New > Android Resource File. The New Resource File dialog appears. Type a name in the File name field, such as "bottom_nav_menu". Select Menu from the Resource type drop-down list, and then click OK.
2.	Add one menu item to nav menu. In bottom_nav_menu.xml
    <menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/navigation_main"
        android:icon="@drawable/ic_home "
        android:title="@string/title_home" />
</menu>
Add the drawable to the project
New > Image asset. The Configure Image Asset dialog appears. Select Icon Type: Action Bar and Tab Icons, name: Home, Asset type radio select Clip Art and click the Clip Art Button. Modal dialog appears, type home in the search and select the home icon. Accept remain defaults.

Add string resource title_home, hover mouse over title_home, click suggestion Create string resource, enter string resource value:  Home

Step 3: Set up to use Navigation graph from Android Jetpack

Edit Main Fragment and Main Activity for using Navigation graph
3.	New > Android Resource File. The New Resource File dialog appears. Type a name in the File name field, such as "nav_graph". Select Navigation from the Resource type drop-down list, and then click OK. To automatically add project dependency, close and reopen the nav_graph tab.
4.	Add fragment main using the design view and rename id to navigation_main and label to "@string/app_name"
5.	Add NavHost to activity_main.xml, replace all with code below
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/fragment_container_view"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        android:paddingBottom="?attr/actionBarSize"
        app:navGraph="@navigation/nav_graph"/>
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/nav_view"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="0dp"
        android:layout_marginEnd="0dp"
        android:background="?android:attr/windowBackground"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_nav_menu" />
</androidx.constraintlayout.widget.ConstraintLayout>

Note:
A)	paddingBottom, so navigation bar not on top of container view 
B)	navGraph, must have this for activity to navigate the fragments,

Step 4. MainFragment.kt 
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val 
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val textMessage:  TextView = binding.message
        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
Note: FragmentMainBinding may appear red, it will be discovered during the compile. The feature in build.gradle, viewBinding true generates this file
Step 5. MainActivity.kt
Replace code with the following:
class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
Compile and run, this is the expected result
Accomplished so far
1.	Updated dependencies to latest versions
2.	Refactored template to use the navigation graph
3.	Created a bottom menu
4.	Add icon to bottom menu
5.	Used strings resource for strings to enable localization
6.	Implemented binding by adding build feature viewBinding to build.gradle and using the binding classes in MainActivity and MainFragment
Step 6. Create Live Data

1.	Add the val to MainViewModel {
val azimuthLiveData = AzimuthLiveData()
2.	Create an inner class that manages the live data
inner class AzimuthLiveData( ) : LiveData<String>() {
	postValue(“Azimuth as degrees”)
}
Note: This is so the code compiles, later adding code.

3.	In MainFragment.kt add this code below the line val textMessage:
appViewModel.azimuthLiveData.observe(viewLifecycleOwner) {
            textMessage.text = it
        }
This is the link for sharing the updated azimuth. The view model declares it as live data, and the fragment observes it. Each time there is a change in the value, the main fragment updates the textMessage text.
4.	Compile and run the code, the screen should display the message Azimuth as degrees
Step 7: Add sensor listeners
1.	Add the highlighted to the inner class 
inner class AzimuthLiveData( ) : LiveData<String>(), SensorEventListener {
2.	An error is displayed to add required functions, add both onAccuracyChanged and onSensorChanged
3.	Only using onSensorChanged, copy this code
override fun onSensorChanged(sensorEvent: SensorEvent) {
            when (sensorEvent.sensor.type) {
                //Cloning those values prevents the data you're currently interested in from being changed by more recent data before you're done with it.
                Sensor.TYPE_ACCELEROMETER -> mAccelerometerData = sensorEvent.values.clone()
                Sensor.TYPE_MAGNETIC_FIELD -> mMagnetometerData = sensorEvent.values.clone()
                else -> return
            }
            val rotationOK = SensorManager.getRotationMatrix(
                rotationMatrix,
                null, mAccelerometerData, mMagnetometerData
            )
            if (rotationOK) {
                /*
                How far the device is oriented or tilted with respect to the Earth's coordinate system. There are three components to orientation:

                Azimuth: The direction (north/south/east/west) the device is pointing. 0 is magnetic north.
                Pitch: The top-to-bottom tilt of the device. 0 is flat.
                Roll: The left-to-right tilt of the device. 0 is flat.
                 */
                // All three angles are measured in radians, and range from -π (-3.141) to π.
                SensorManager.getOrientation(rotationMatrix, orientationValues)
                var azimuth = orientationValues[0] * (180/Math.PI)
                if (azimuth < 0) {azimuth += 360}
                var pitch = orientationValues[1] * (180/Math.PI) // positive points down, higher - is bigger angle
                var roll = orientationValues[2] * (180/Math.PI) // negative is left doww
                if (Math.abs(pitch) < VALUE_DRIFT) {
                    pitch = 0.0
                }
                if (Math.abs(roll) < VALUE_DRIFT) {
                    roll = 0.0
                }
                postValue("Azimuth as degrees: $azimuth \n pitch as degrees: $pitch \n roll as degrees: $roll") // sensor name: ${event.sensor.name} Incline array: $inclinationArray[0]") (pitch -45 degrees means tilt up 45 degrees
            }
        }
Note that post value is changed to display the values of the azimuth, pitch and roll.
4.	Add the events to turn on and off the sensors
override fun onActive() {
            Log.d("***** Entering onActive", this.toString())
            sensorManager.let { sm ->
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).let {
                    sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)

                    if (it != null) {
                        Log.d("***** Register sensor: ", it.name)
                    }
                }
            }
            sensorManager.let { sm ->
                sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).let {
                    sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)

                    if (it != null) {
                        Log.d("***** Register sensor: ", it.name)
                    }
                }

            }
        }
        override fun onInactive() {
            sensorManager.unregisterListener(this)
        }
	Note: Logging is implemented for testing verification that sensors are enabled.

This is the final app

Add Data Classes
In build.gradle (app) add
Plugins { id 'kotlin-parcelize'
Dependencies implementation 'com.google.code.gson:gson:2.9.0'

Create package at same level as ui, named model
New -> Kotlin Class -> Data Class
@Parcelize
data class Rank(
    @SerializedName("Id") val id: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Date") val date: String? = null,
    @SerializedName("Description") val description: String
): Parcelable

Create the data file
Under the model package create new package data
Create new file Kotlin Class -> Object RankRepository

object RankRepository {
    val ranks = flowOf(
        listOf(
            Rank() // this is the details of each record, comma separated.
        )
    )
}
listOf(
    Rank(0,"Recruit", "x",null, ""),
    Rank(1,"a", "a",null, ""),
    Rank(2,"b", "b",null, ""),
    Rank(3,"c", "c",null, ""),
    Rank(4,"d", "d",null, ""),
    Rank(5,"e", "e",null, ""),
    Rank(6,"f", "f",null, ""),
    Rank(7,"g", "g",null, ""),
    Rank(8,"h", "h",null, ""),
    Rank(9,"i", "i",null, "")
)

Modify res/ menu for 3 tabs
Open bottom_nav_menu and add this code
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/navigation_main"
        android:icon="@drawable/ic_home"
        android:title="@string/title_home" />
    <item
        android:id="@+id/navigation_rank"
        android:icon="@drawable/ic_home"
        android:title="@string/title_rank" />
    <item
        android:id="@+id/navigation_other"
        android:icon="@drawable/ic_home"
        android:title="@string/title_other" />
</menu>
New package for Rank UI

Create new package, ui.rank (make sure to remove .main text, if defaulted), .main is replaced by .rank. it should be at the same level as main
Create new -> Fragment -> Fragment with View Model and edit names, replace Blank with Rank. 
RankFragment. Remove companion object. onCreateView(). Move ViewModelProvider( to onCreateView then delete onActivityCreated. Add vars _binding and binding (see MainFragment) and add appView Model. 
Initialize binding in onCreate (see MainFragment)
Verify: _binding = FragmentRankBinding?
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/navigation_main">

    <fragment
        android:id="@+id/navigation_main"
        android:name="com.dotrothschild.mysamplesensorswithlivedata.ui.main.MainFragment"
        android:label="@string/app_name"
        tools:layout="@layout/fragment_main" >
        <action
            android:id="@+id/action_navigation_main_to_navigation_rank"
            app:destination="@id/navigation_rank" />
    </fragment>
    <fragment
        android:id="@+id/navigation_rank"
        android:name="com.dotrothschild.mysamplesensorswithlivedata.ui.rank.RankFragment"
        android:label="fragment_rank"
        tools:layout="@layout/fragment_rank" >
        <action
            android:id="@+id/action_navigation_rank_to_navigation_main"
            app:destination="@id/navigation_main" />
    </fragment>
</navigation>

Copy the function: Destroy() from MainFragment

Add fragment to nav_graph 
Add rankFragment and rename id to navigation_rank. You will get a “problem message”. This is good, the id must be the same name as the id in the menu (created above). Drag point (makes line from main to rank and another from rank to main

Navigation using bottom menu
Create sealed class for navigation direction New -> Sealed Class GetBottomMenuFragment. Should be at same level as MainActivity.  Why do it this way?  Answer: It makes cleaner coded if the change menu does more than just navigate.

sealed class GetBottomMenuFragment {
    object Home : GetBottomMenuFragment()
    object Rank : GetBottomMenuFragment()
    object Other : GetBottomMenuFragment()
}

MainActivity add the variable   lateinit var bottomMenuFragment: GetBottomMenuFragment
In onCreate( method 
Initialize var, bottomMenuFragement = GetBottomMenuFragment.Home
add fun call sutpNavConroller and create the function

Copy this code for setupNavController()

private fun setupNavController() {
    // https://stackoverflow.com/questions/58703451/fragmentcontainerview-as-navhostfragment
    val navHostFragment = supportFragmentManager
        .findFragmentById(R.id.fragment_container_view) as NavHostFragment
    val navController = navHostFragment.navController
    // end bug fix work around

    NavigationUI.setupWithNavController(
        binding.navView,
        navController
    )
}

Show all Ranks 
To display a list of records, the following is completed
1.	Create a recycler view of the item (record) in res/layout recyclerview_item_rank.xml.
2.	Create an adapter that binds the item to the to the recyclerview
3.	Modify fragment_rank to display the recycler view
4.	Modify MainViewModel to load RankRepository
5.	Modify the RankFragment
Create layout
Layout -> New -> New Resource file. Name it recyclerview_item_rank
Card layout design here is based on one I use. The point is there is a field for displaying the data of a record. Naming is critically important.

Add the following first
Create dimens file in res/values New->Values Resource File -> filename dimens
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="single_margin">16dp</dimen>
    <dimen name="cardViewElevation">2dp</dimen>
</resources>

Create a cardview style file, this is based on what I use
Create styles file in res/values

Add this color to res/colors
<color name="cardRippleColor">#d1d4e2</color>

Code in recyclerview_item_rank.xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/adapter_rank_name"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="28dp"
        android:layout_marginTop="@dimen/single_margin"
        android:layout_marginEnd="28dp"
        android:textAllCaps="true"
        android:textColor="@color/black"
        android:textSize="18sp"
        android:visibility="visible"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:text="40" />

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/cardView"
        style="@style/CardViewStyle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="28dp"
        android:layout_marginEnd="28dp"
        android:layout_marginBottom="8dp"
        android:clickable="true"
        android:focusable="true"
        app:cardCornerRadius="8dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/adapter_rank_name">


        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/itemSelectView"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            <TextView
                android:id="@+id/adapter_rank_date"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginStart="4dp"
                android:layout_marginTop="4dp"
                android:textColor="@color/white"
                android:textSize="18sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.0"
                app:layout_constraintStart_toEndOf="@+id/adapter_image_rank"
                app:layout_constraintTop_toTopOf="parent"
                tools:text="This is limited to 1 line" />


            <TextView
                android:id="@+id/adapter_rank_description"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginEnd="@dimen/single_margin"
                android:textColor="@color/white"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="@+id/adapter_rank_date"
                app:layout_constraintTop_toBottomOf="@+id/adapter_rank_date"
                tools:text="blank line" />
            <ImageView
                android:id="@+id/adapter_image_rank"
                android:layout_width="70dp"
                android:layout_height="0dp"
                android:scaleType="fitXY"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/x" />

        </androidx.constraintlayout.widget.ConstraintLayout>
    </com.google.android.material.card.MaterialCardView>
</androidx.constraintlayout.widget.ConstraintLayout>

Create adapter view
Adapter is mostly boilerplate code; issues are in naming and brackets. When copying this code, rename, use find and replace, it is more accurate than refactoring and enabe Cc (caps case)
New -> Kotlin Class, name: RankAdapter
Copy and replace using Cc Rank <YourVarName> (10 times) (import the data class from model)
Again replace rank <yourVarName> (17 times)

package com.dotrothschild.mysamplesensorswithlivedata.ui.rank

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.mysamplesensorswithlivedata.R
import com.dotrothschild.mysamplesensorswithlivedata.databinding.RecyclerviewItemRankBinding
import com.dotrothschild.mysamplesensorswithlivedata.model.Rank

class RankAdapter (
    private val context: Context,
    private val ranks: List<Rank>,
    private val rankCallback: (Rank, Int) -> Unit
) :
    RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    inner class RankViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerviewItemRankBinding.bind(view)
        fun bind(rank: Rank, position: Int) {
            binding.apply {
                adapterRankName.text = rank.name
                adapterRankDate.text = rank.date
                adapterRankDescription.text = rank.description
                when (rank.id)                 {
                    1 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.a))) //
                    2 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.b))) //
                    3 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.c))) //
                    4 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.d))) //
                    5 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.e))) //
                    6 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.f))) //
                    7 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.g))) //
                    8 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.h))) //
                    9 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.i))) //
                    else -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.x))) // recruit
                }
                itemSelectView.setBackgroundResource(Color.LTGRAY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RankViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_rank, parent, false)
        )


    override fun onBindViewHolder(holder: RankAdapter.RankViewHolder, position: Int) {
        ranks[position].let { rank ->
            holder.bind(rank, position)
            // here goes the onclick event
            holder.binding.cardView.setOnClickListener {
                rankCallback.invoke(rank, position)
            }
        }
    }

    override fun getItemCount() = ranks.size
}

Modify frgrament Rank XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/list"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.rank.RankFragment">

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/topLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardElevation="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <TextView
                android:id="@+id/text_settings_header"
                android:text="@string/all_ranks"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:textColor= "@color/white"
                android:textSize="20sp"
                android:textAlignment="center"
                tools:text="Read select location " />
        </androidx.constraintlayout.widget.ConstraintLayout>
    </com.google.android.material.card.MaterialCardView>
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:clipToPadding="false"
        android:paddingTop="@dimen/single_margin"
        android:paddingBottom="@dimen/single_margin"
        android:visibility="visible"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/topLayout"
        tools:listitem="@layout/recyclerview_item_rank" />
</androidx.constraintlayout.widget.ConstraintLayout>



AppViewModel
Create the app view model to hold application data. This is for the life of the application
New -> Class Kotlin folder UI name: AppViewModel

class AppViewModel
    (
    val rankRepository: RankRepository,
    applicationContext: Context
) : ViewModel() {}

class AppViewModelFactory(
    val rankRepository: RankRepository,
    private val applicationContext: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(
                rankRepository,
                applicationContext
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

Modify MainViewModel
This holds the rank repository. I can also be in rank fragment view model, but will go out of scope when navigating away from the fragment. 

Add the val
class MainViewModel(
    val rankRepository: RankRepository

Modify MainFragment to pass the rank repositiry

Create a Utility to Flatten a list
FlattenFlowList.kt
suspend fun <T> Flow<List<T>>.flattenToList() =
    flatMapConcat { it.asFlow() }.toList()

Modify Rank Fragment kt
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    viewModel = ViewModelProvider(this)[RankViewModel::class.java]
    _binding = FragmentRankBinding.inflate(inflater, container, false)

    val scope = MainScope()
    scope.launch {
        setupView(appViewModel.rankRepository.ranks.flattenToList())
    }
    return binding.root
}

private fun setupView(flatList: List<Rank>) {

    binding.apply {
       // topLayout.setBackgroundResource(R.drawable.cardview_bg)
        adapter = RankAdapter(
            requireContext(),
            flatList
        )
        { rank, _ ->
            appViewModel.rank = rank
        }
        recyclerView.adapter = adapter
        binding.apply {
            recyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(0)
        }
    } // end binding apply
}


![image](https://user-images.githubusercontent.com/31523304/180643840-6ad52d1f-88d6-47d3-8781-65df115811c3.png)
