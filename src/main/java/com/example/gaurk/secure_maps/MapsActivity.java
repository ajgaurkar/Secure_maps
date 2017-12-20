package com.example.gaurk.secure_maps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View circle_view;
    private ImageView ctr_pin;
    private ImageView options_image_view;
    private RelativeLayout heat_map_layout;
    private RelativeLayout selection_confirm_layout;
    private Button select_cood_btn;
    private Button cancel_cood_btn;
    private TextView selected_latlong_text_view;
    private FloatingActionButton filterFab;
    private FloatingActionButton add_item_Fab;
    private LatLng center_latlong;
    private HeatmapTileProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        circle_view = (View) findViewById(R.id.circle_view);
        ctr_pin = (ImageView) findViewById(R.id.center_pin);
        heat_map_layout = (RelativeLayout) findViewById(R.id.heat_map_layout);
        selection_confirm_layout = (RelativeLayout) findViewById(R.id.selection_confirm_layout);
        selected_latlong_text_view = (TextView) findViewById(R.id.selected_latlong);
        filterFab = (FloatingActionButton) findViewById(R.id.filter_fab);
        add_item_Fab = (FloatingActionButton) findViewById(R.id.add_item_btn);


        //listener for add new entry button
        add_item_Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctr_pin.setVisibility(View.VISIBLE);
                circle_view.setVisibility(View.INVISIBLE);
                heat_map_layout.setVisibility(View.INVISIBLE);
                selection_confirm_layout.setVisibility(View.VISIBLE);

                LayoutInflater flater = LayoutInflater.from(MapsActivity.this);

                View addItemDialogView = flater.inflate(R.layout.item_add_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setView(addItemDialogView);
                builder.setCancelable(false);
                EditText item_name_edit_text = (EditText) addItemDialogView.findViewById(R.id.incident_header_text_view);
                EditText item_detail_edit_text = (EditText) addItemDialogView.findViewById(R.id.new_item_detail_EditText);
                RadioGroup qualification_radio_group = (RadioGroup) addItemDialogView.findViewById(R.id.qualification_radio_group);

                builder.setTitle("New Input");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("XXXXXX ");
                        heat_map_layout.setVisibility(View.VISIBLE);
                        selection_confirm_layout.setVisibility(View.INVISIBLE);

                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("YYYYYY : ");
                        heat_map_layout.setVisibility(View.VISIBLE);
                        selection_confirm_layout.setVisibility(View.INVISIBLE);

                    }
                });
                builder.show();


            }
        });

        //listener for filter button
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater flater = LayoutInflater.from(MapsActivity.this);

                View addItemDialogView = flater.inflate(R.layout.filter_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setView(addItemDialogView);
                builder.setCancelable(false);
                EditText item_name_edit_text = (EditText) addItemDialogView.findViewById(R.id.incident_header_text_view);
                EditText item_detail_edit_text = (EditText) addItemDialogView.findViewById(R.id.new_item_detail_EditText);
                RadioGroup qualification_radio_group = (RadioGroup) addItemDialogView.findViewById(R.id.qualification_radio_group);

                builder.setTitle("Add filter");
                builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("XXXXXX ");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("YYYYYY : ");

                    }
                });
                builder.show();
            }
        });
    }


    //google map methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Add a marker in Sydney and move the camera/current location
        LatLng centennial_High_School = new LatLng(39.294541, -76.613114);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_bmp);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centennial_High_School, 14.0f));

        center_latlong = mMap.getCameraPosition().target;
        System.out.println("center_latlong : " + center_latlong);

        float zoom = mMap.getCameraPosition().zoom;
        System.out.println("zoom : " + zoom);


        LatLng police_stn_1 = new LatLng(39.322534, -76.602527);
        LatLng police_stn_2 = new LatLng(39.303205, -76.666369);
        LatLng police_stn_3 = new LatLng(39.278496, -76.625908);
        LatLng police_stn_4 = new LatLng(39.343984, -76.640925);
        LatLng hosp_1 = new LatLng(39.317798, -76.614788);
        LatLng hosp_2 = new LatLng(39.303466, -76.631717);
        LatLng hosp_3 = new LatLng(39.288018, -76.624812);
        LatLng hosp_4 = new LatLng(39.292341, -76.596476);
        LatLng hosp_5 = new LatLng(39.288679, -76.649355);
        LatLng hosp_6 = new LatLng(39.303466, -76.631717);
        LatLng hosp_7 = new LatLng(39.345209, -76.622208);
        LatLng hosp_8 = new LatLng(39.307385, -76.680034);
        LatLng camp_1 = new LatLng(39.346048, -76.609995);
        LatLng camp_2 = new LatLng(39.280838, -76.671105);
        LatLng camp_3 = new LatLng(39.290820, -76.576817);


        mMap.addMarker(new MarkerOptions().position(police_stn_1).title("Police station, 21334").icon(BitmapDescriptorFactory.fromResource(R.drawable.police_station_32)));
        mMap.addMarker(new MarkerOptions().position(police_stn_2).title("Police station, 21774").icon(BitmapDescriptorFactory.fromResource(R.drawable.police_station_32)));
        mMap.addMarker(new MarkerOptions().position(police_stn_3).title("Police station, 21227").icon(BitmapDescriptorFactory.fromResource(R.drawable.police_station_32)));
        mMap.addMarker(new MarkerOptions().position(police_stn_4).title("Police station, 21212").icon(BitmapDescriptorFactory.fromResource(R.drawable.police_station_32)));
        mMap.addMarker(new MarkerOptions().position(hosp_1).title("Care center, 21526").snippet("Open today, 10am - 5pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
//        mMap.addMarker(new MarkerOptions().position(hosp_2).title("Care center, 21142").snippet("Open today, 08am - 9pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
//        mMap.addMarker(new MarkerOptions().position(hosp_3).title("Care center, 21312").snippet("Open today, 10am - 3pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
        mMap.addMarker(new MarkerOptions().position(hosp_4).title("Care center, 21251").snippet("Open today, 08am - 5pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
//        mMap.addMarker(new MarkerOptions().position(hosp_5).title("Care center, 21116").snippet("Open today, 10am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
        mMap.addMarker(new MarkerOptions().position(hosp_6).title("Care center, 21172").snippet("Open today, 09am - 5pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
        mMap.addMarker(new MarkerOptions().position(hosp_7).title("Care center, 21635").snippet("Open today, 10am - 5pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
        mMap.addMarker(new MarkerOptions().position(hosp_8).title("Care center, 21113").snippet("Open today, 10am - 5pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_green_32)));
        mMap.addMarker(new MarkerOptions().position(camp_3).title("Drug rehab event, 21227").snippet("Oct 27, 11am - 5pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_32_trai)));
        mMap.addMarker(new MarkerOptions().position(camp_2).title("Public Naloxone Training, 21213").snippet("Oct 27, 9:30am - 11:30pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_32_trai))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(camp_1).title("Drug rehab Event, 21223").snippet("Oct 22, 1pm - 3pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_32_trai)));

        //test method. used for debugging
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                System.out.println("LAT LONG : " + latLng);
                mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        //test method. used for debugging
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                System.out.println("CAMERA MOVE ");
                float zoom = mMap.getCameraPosition().zoom;
                System.out.println("zoom : " + zoom);

                center_latlong = mMap.getCameraPosition().target;
                System.out.println("center_latlong : " + center_latlong);
                selected_latlong_text_view.setText("" + center_latlong);

            }
        });

        //create WeightedLatLng objects for heat map point with intensity values
        WeightedLatLng data1 = new WeightedLatLng(new LatLng(-8.579121, -53.941000), 11.5);
        WeightedLatLng data2 = new WeightedLatLng(new LatLng(-8.624258, -54.000569), 10.5);
        WeightedLatLng data3 = new WeightedLatLng(new LatLng(-8.649778, -53.792961), 17.5);
        WeightedLatLng data4 = new WeightedLatLng(new LatLng(-8.732619, -53.689958), 20.5);
        WeightedLatLng data5 = new WeightedLatLng(new LatLng(-8.732534, -53.939404), 51.5);
        WeightedLatLng data6 = new WeightedLatLng(new LatLng(-8.840186, -53.933887), 51.5);

        WeightedLatLng data7 = new WeightedLatLng(new LatLng(-8.009465, -51.997139), 26.5);
        WeightedLatLng data8 = new WeightedLatLng(new LatLng(-9.041986, -51.946296), 51.5);
        WeightedLatLng data9 = new WeightedLatLng(new LatLng(-7.467518, -55.098998), 20.5);
        WeightedLatLng data10 = new WeightedLatLng(new LatLng(-8.271234, -53.435944), 51.5);
        WeightedLatLng data11 = new WeightedLatLng(new LatLng(-8.741564, -53.168021), 31.5);
        WeightedLatLng data12 = new WeightedLatLng(new LatLng(-7.193998, -53.876551), 51.5);
        WeightedLatLng data13 = new WeightedLatLng(new LatLng(-10.660135, -53.565427), 40.5);

        WeightedLatLng data14 = new WeightedLatLng(new LatLng(-7.685022, -54.112256), 20.5);
        WeightedLatLng data15 = new WeightedLatLng(new LatLng(-8.624677, -55.539919), 30.5);
        WeightedLatLng data16 = new WeightedLatLng(new LatLng(-7.561254, -54.535173), 10.5);
        WeightedLatLng data17 = new WeightedLatLng(new LatLng(-8.754483, -53.433716), 10.5);
        WeightedLatLng data18 = new WeightedLatLng(new LatLng(-8.215050, -52.563807), 20.5);
        WeightedLatLng data19 = new WeightedLatLng(new LatLng(-8.122489, -52.275051), 10.5);
        WeightedLatLng data20 = new WeightedLatLng(new LatLng(-10.660135, -53.565427), 30.5);

        ArrayList<WeightedLatLng> weightedLatLngs = new ArrayList<>();

        weightedLatLngs.add(data1);
        weightedLatLngs.add(data2);
        weightedLatLngs.add(data3);
        weightedLatLngs.add(data4);
        weightedLatLngs.add(data5);
        weightedLatLngs.add(data6);
        weightedLatLngs.add(data7);
        weightedLatLngs.add(data8);
        weightedLatLngs.add(data9);
        weightedLatLngs.add(data10);
        weightedLatLngs.add(data11);
        weightedLatLngs.add(data12);
        weightedLatLngs.add(data13);
        weightedLatLngs.add(data14);
        weightedLatLngs.add(data15);
        weightedLatLngs.add(data16);
        weightedLatLngs.add(data17);
        weightedLatLngs.add(data18);
        weightedLatLngs.add(data19);
        weightedLatLngs.add(data20);

        //same as above(direct method : without intermediate object creation)
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28566933	,-76.63666684), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34551398	,-76.68433829), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31228473	,-76.64572852), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29494992	,-76.58209022), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29446236	,-76.67844858), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29380366	,-76.68416022), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30489596	,-76.57963255), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30489596	,-76.57963255), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32981072	,-76.69057034), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30444931	,-76.59375324), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30148595	,-76.58108155), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29467585	,-76.65846817), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29467585	,-76.65846817), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3384935	,-76.65854523), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28933249	,-76.65199559), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30143372	,-76.66368772), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27771888	,-76.5424693), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.24547561	,-76.62879156), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33635852	,-76.53086802), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2910585	,-76.63240249), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28265068	,-76.65259253), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2987788	,-76.58516652), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23186454	,-76.59261829), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31856613	,-76.56463566), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3706656	,-76.60976968), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28853365	,-76.59154567), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30546281	,-76.58125159), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30941799	,-76.6417728), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30057701	,-76.54629505), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33212569	,-76.63768192), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31335834	,-76.6042443), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35859908	,-76.67920073), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35535717	,-76.60827203), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31532453	,-76.57973573), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31008698	,-76.58512725), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28117063	,-76.64952253), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30572069	,-76.58465524), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30572069	,-76.58465524), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29690271	,-76.5784948), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29692643	,-76.64744845), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32289256	,-76.57376941), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30983418	,-76.70778696), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35076652	,-76.67051019), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35076652	,-76.67051019), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35076652	,-76.67051019), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31361272	,-76.68484746), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30503314	,-76.67072573), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33736035	,-76.60708073), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33736035	,-76.60708073), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3379089	,-76.66783762), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28422389	,-76.64500324), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28829033	,-76.67607579), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.24587129	,-76.62746271), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31146844	,-76.63810132), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32364871	,-76.53727016), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27870124	,-76.54232799), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27870124	,-76.54232799), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31146515	,-76.60929361), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23613631	,-76.60165388), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29668415	,-76.58502897), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34886825	,-76.67864817), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29624878	,-76.58139297), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29624878	,-76.58139297), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2957035	,-76.58143571), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.36797525	,-76.55027056), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28718518	,-76.5887281), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28365083	,-76.64497198), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28365083	,-76.64497198), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2957035	,-76.58143571), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28887374	,-76.59665328), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27405993	,-76.62957977), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3114802	,-76.5845345), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28991663	,-76.60207621), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3397486	,-76.68580338), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28541418	,-76.69382392), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29246665	,-76.60784912), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30193513	,-76.59786654), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34504311	,-76.66242165), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27775747	,-76.69147091), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34241772	,-76.68188207), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28863547	,-76.56775616), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3309699	,-76.60891163), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3309699	,-76.60891163), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32743949	,-76.68259147), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28754033	,-76.6486679), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28766711	,-76.64860465), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28992722	,-76.63826016), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31503444	,-76.63032625), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2821667	,-76.64369923), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31453776	,-76.59990389), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31453776	,-76.59990389), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29689309	,-76.63330977), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30572069	,-76.58465524), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31466303	,-76.59986595), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29679686	,-76.69173368), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29679686	,-76.69173368), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.24770177	,-76.62896548), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.24770177	,-76.62896548), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.24770177	,-76.62896548), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29513037	,-76.65793026), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32967977	,-76.56550481), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29696898	,-76.63111628), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29944966	,-76.58308523), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29944966	,-76.58308523), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34514043	,-76.59947498), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30724501	,-76.70937827), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31035129	,-76.61492643), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31035129	,-76.61492643), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33074905	,-76.53167409), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31035129	,-76.61492643), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31222974	,-76.59213884), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32799101	,-76.55635409), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28678731	,-76.6469923), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29520682	,-76.64654363), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29520682	,-76.64654363), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31079984	,-76.59214135), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22889965	,-76.60241347), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28180675	,-76.65115381), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35272813	,-76.60672066), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35751304	,-76.5821667), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32660139	,-76.71037404), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28193451	,-76.63666653), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29748836	,-76.64854298), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31187875	,-76.6095098), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3541101	,-76.59413929), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33410586	,-76.67365892), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28951997	,-76.63221901), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30221068	,-76.58538188), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31551091	,-76.59363797), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28877237	,-76.63400793), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29522367	,-76.64606598), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31726298	,-76.67520794), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.26156986	,-76.66656637), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29064626	,-76.56670936), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29895344	,-76.6458949), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31992517	,-76.57474026), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23608957	,-76.60969145), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23608957	,-76.60969145), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33821248	,-76.60223367), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28783848	,-76.66023623), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30915386	,-76.63747131), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30915386	,-76.63747131), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30915386	,-76.63747131), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28327292	,-76.65615934), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30533986	,-76.64540646), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34830442	,-76.67485675), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30013615	,-76.63908522), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3095149	,-76.63487437), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29564036	,-76.63569776), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.36273586	,-76.55248994), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2951614	,-76.68036214), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29729074	,-76.66189367), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29903982	,-76.6395316), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29284711	,-76.62124381), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3038534	,-76.54094671), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3038534	,-76.54094671), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3038534	,-76.54094671), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32957995	,-76.66074235), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2839441	,-76.63932393), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32302844	,-76.59019073), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34664763	,-76.69023309), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33669498	,-76.65640226), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29856799	,-76.5979944), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29856799	,-76.5979944), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29856799	,-76.5979944), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29856799	,-76.5979944), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30378129	,-76.58098233), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34692326	,-76.53302467), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35222026	,-76.6824162), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23149732	,-76.59530752), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23149732	,-76.59530752), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29090617	,-76.56805573), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31862008	,-76.62203524), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31095194	,-76.63877674), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31095194	,-76.63877674), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31095194	,-76.63877674), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31080602	,-76.63872134), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32002599	,-76.69237741), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33674696	,-76.68291046), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29403496	,-76.60417376), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31527618	,-76.67349713), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28410213	,-76.63851805), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28410213	,-76.63851805), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29179764	,-76.65199982), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29179764	,-76.65199982), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32245518	,-76.67653653), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.36838271	,-76.58871789), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28768018	,-76.63468875), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28768018	,-76.63468875), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28768018	,-76.63468875), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32372828	,-76.67383341), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35360412	,-76.70641256), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30989981	,-76.66118848), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30989981	,-76.66118848), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30989981	,-76.66118848), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29032975	,-76.65465835), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29032975	,-76.65465835), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30524125	,-76.63261249), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30524125	,-76.63261249), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29804586	,-76.57732855), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29804586	,-76.57732855), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29804586	,-76.57732855), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29804586	,-76.57732855), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33448439	,-76.53691989), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33448439	,-76.53691989), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34064613	,-76.66879136), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29875786	,-76.64764787), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32852089	,-76.60812063), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31442764	,-76.6451982), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27658924	,-76.54254363), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30229155	,-76.6845712), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34695595	,-76.60920551), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30783809	,-76.58636699), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30887526	,-76.65262507), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30887526	,-76.65262507), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29376036	,-76.68514681), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29376036	,-76.68514681), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23882009	,-76.60465859), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29376036	,-76.68514681), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29376036	,-76.68514681), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27974596	,-76.6241827), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33647168	,-76.54787125), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28428018	,-76.68337501), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30793044	,-76.58392784), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30793044	,-76.58392784), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31636053	,-76.67305532), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29197644	,-76.61551619), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29197644	,-76.61551619), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30470696	,-76.66677851), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30470696	,-76.66677851), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31605119	,-76.59841884), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33813619	,-76.60527217), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33813619	,-76.60527217), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33159393	,-76.60941862), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30096573	,-76.58155861), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30924225	,-76.5746193), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30000843	,-76.57888941), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30912505	,-76.5829458), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3322676	,-76.68761846), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32748769	,-76.55472341), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32135219	,-76.68367282), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32842863	,-76.66742526), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33287044	,-76.60943065), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23044538	,-76.590517), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31879097	,-76.62208311), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.32137538	,-76.59339262), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29867676	,-76.59715498), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34559249	,-76.67056468), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.26517188	,-76.65313265), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27969479	,-76.65800052), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27969479	,-76.65800052), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34685693	,-76.59596114), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31360277	,-76.66955336), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29265586	,-76.66343501), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30724501	,-76.70937827), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30724501	,-76.70937827), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30724501	,-76.70937827), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3334758	,-76.6895724), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28583496	,-76.55567537), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28583496	,-76.55567537), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28583496	,-76.55567537), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33201557	,-76.69990098), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28302703	,-76.68803703), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31306334	,-76.6746656), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28302703	,-76.68803703), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28756192	,-76.54446684), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29749912	,-76.58731403), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29749912	,-76.58731403), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29414327	,-76.67434361), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23335476	,-76.59532694), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23335476	,-76.59532694), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23335476	,-76.59532694), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23335476	,-76.59532694), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22328161	,-76.58824672), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.2232609	,-76.58831555), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.3056602	,-76.6721505), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30044211	,-76.59521794), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31334567	,-76.66841163), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29640431	,-76.64761631), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.28291839	,-76.64280285), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35176751	,-76.66625972), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35176751	,-76.66625972), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35176751	,-76.66625972), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35176751	,-76.66625972), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22446396	,-76.58965327), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22446396	,-76.58965327), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22446396	,-76.58965327), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22446396	,-76.58965327), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23559153	,-76.6062043), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23559153	,-76.6062043), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23559153	,-76.6062043), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23559153	,-76.6062043), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30537412	,-76.64960084), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.31492799	,-76.68177967), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33033753	,-76.56640906), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.34303841	,-76.66712124), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.35118112	,-76.66872559), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23152604	,-76.59054782), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23152604	,-76.59054782), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29866929	,-76.59722531), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27200411	,-76.59205497), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.27200411	,-76.59205497), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23020494	,-76.59146674), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23020494	,-76.59146674), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23020494	,-76.59146674), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.23719658	,-76.60509454), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30560097	,-76.58785734), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33758062	,-76.66853944), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.33758062	,-76.66853944), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.22754149	,-76.58989409), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.30585199	,-76.66936313), 100.5));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(39.29504854	,-76.63250201), 100.5));


        //Heat map point colors
        int[] colors = {
                Color.rgb(255, 200, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);
        mProvider = new HeatmapTileProvider.Builder().weightedData(weightedLatLngs).gradient(gradient).build();
        TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }
}
