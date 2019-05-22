package com.example.allison.bigfootsmallfoot;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import androidx.navigation.NavController;
        import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment finalHost = NavHostFragment.create(R.navigation.nav_graph);
        NavController navController = finalHost.getNavController();
    }
}
