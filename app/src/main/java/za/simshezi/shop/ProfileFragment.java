package za.simshezi.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Objects;

import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.UserModel;


public class ProfileFragment extends Fragment {
    private LinearLayout layoutCards;
    private LinearLayout layoutWallet;
    private LinearLayout layoutPromotions;
    private LinearLayout layoutHelp;
    private LinearLayout layoutAbout;
    private LinearLayout layoutPrivacy;
    private LinearLayout layoutSettings;
    private ImageView imgProfile;
    private Button btnSignOut;
    private SerializableModel model;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutCards = view.findViewById(R.id.layoutProfileCards);
        layoutWallet = view.findViewById(R.id.layoutProfileWallet);
        layoutPromotions = view.findViewById(R.id.layoutProfilePromotions);
        layoutHelp = view.findViewById(R.id.layoutProfileHelp);
        layoutAbout = view.findViewById(R.id.layoutProfileAbout);
        layoutPrivacy = view.findViewById(R.id.layoutProfilePrivacy);
        layoutSettings = view.findViewById(R.id.layoutProfileSettings);
        imgProfile = view.findViewById(R.id.imgProfile);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        build();
    }

    private void build() {
        layoutCards.setOnClickListener(onLayoutClicked(CardPaymentsActivity.class));
        layoutWallet.setOnClickListener(onLayoutClicked(CreditWalletActivity.class));
        layoutPromotions.setOnClickListener(onLayoutClicked(PromotionsActivity.class));
        layoutHelp.setOnClickListener(onLayoutClicked(HelpActivity.class));
        layoutAbout.setOnClickListener(onLayoutClicked(AboutActivity.class));
        layoutPrivacy.setOnClickListener(onLayoutClicked(PrivacyActivity.class));
        layoutSettings.setOnClickListener(onLayoutClicked(SettingsActivity.class));
        imgProfile.setOnClickListener(onLayoutClicked(ProfileDetailsActivity.class));
        btnSignOut.setOnClickListener((view) -> {
            Activity activity = requireActivity();
            SharedPreferences sharedpreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            activity.finishAffinity();
        });
    }

    @NonNull
    private View.OnClickListener onLayoutClicked(Class<?> clazz) {
        CartModel cart = null;
        if (model != null) {
            cart = (CartModel) model.getModel();
        }
        CartModel finalCart = cart;
        return (view -> {
            Intent intent = new Intent(getContext(), clazz);
            intent.putExtra("cart", finalCart);
            startActivity(intent);
        });
    }

    public void setModel(SerializableModel model) {
        this.model = model;
    }
}