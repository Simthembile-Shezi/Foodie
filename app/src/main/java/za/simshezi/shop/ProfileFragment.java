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
import android.widget.TextView;

import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.UserModel;


public class ProfileFragment extends Fragment {
    public static int PROFILE_DEST = 3;
    private LinearLayout layoutCards;
    private LinearLayout layoutWallet;
    private LinearLayout layoutPromotions;
    private LinearLayout layoutHelp;
    private LinearLayout layoutAbout;
    private LinearLayout layoutPrivacy;
    private LinearLayout layoutSettings;
    private ImageView imgProfile;
    private Button btnSignOut;
    private TextView tvName, tvEmail;

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
        tvName = view.findViewById(R.id.tvProfileFullname);
        tvEmail = view.findViewById(R.id.tvProfileEmail);
        build();
    }

    private void build() {
        CartModel cart = (CartModel) requireActivity().getIntent().getSerializableExtra("cart");
        if(cart != null) {
            UserModel user = cart.getUser();
            tvEmail.setText(user.getEmail());
            tvName.setText(user.getName());
            layoutCards.setOnClickListener(onLayoutClicked(CardPaymentsActivity.class, cart));
            layoutWallet.setOnClickListener(onLayoutClicked(CreditWalletActivity.class, cart));
            layoutPromotions.setOnClickListener(onLayoutClicked(PromotionsActivity.class, cart));
            layoutHelp.setOnClickListener(onLayoutClicked(HelpActivity.class, cart));
            layoutAbout.setOnClickListener(onLayoutClicked(AboutActivity.class, cart));
            layoutPrivacy.setOnClickListener(onLayoutClicked(PrivacyActivity.class, cart));
            layoutSettings.setOnClickListener(onLayoutClicked(SettingsActivity.class, cart));
            imgProfile.setOnClickListener(onLayoutClicked(ProfileDetailsActivity.class, cart));
            btnSignOut.setOnClickListener((view) -> {
                Activity activity = requireActivity();
                SharedPreferences sharedpreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                activity.finishAffinity();
            });
        }
    }

    @NonNull
    private View.OnClickListener onLayoutClicked(Class<?> clazz, CartModel cart) {
        return (view -> {
            Intent intent = new Intent(getContext(), clazz);
            intent.putExtra("cart", cart);
            startActivity(intent);
        });
    }
}