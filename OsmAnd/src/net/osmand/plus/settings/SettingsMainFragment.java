package net.osmand.plus.settings;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.Preference;

import net.osmand.plus.ApplicationMode;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.profiles.SettingsProfileActivity;
import net.osmand.util.Algorithms;

import static net.osmand.plus.profiles.EditProfileFragment.MAP_CONFIG;
import static net.osmand.plus.profiles.EditProfileFragment.OPEN_CONFIG_ON_MAP;
import static net.osmand.plus.profiles.EditProfileFragment.SELECTED_ITEM;

public class SettingsMainFragment extends BaseSettingsFragment {

	public static final String TAG = "SettingsMainFragment";

	@Override
	protected int getPreferencesResId() {
		return R.xml.settings_main_screen;
	}

	@Override
	protected int getToolbarResId() {
		return R.layout.global_preference_toolbar;
	}

	@Override
	protected String getToolbarTitle() {
		return getString(R.string.shared_string_settings);
	}

	@Override
	public int getStatusBarColorId() {
		return isNightMode() ? R.color.status_bar_color_light : R.color.status_bar_color_dark;
	}

	@Override
	protected void setupPreferences() {
		Preference personalAccount = findPreference("personal_account");
		Preference globalSettings = findPreference("global_settings");

		personalAccount.setIcon(getContentIcon(R.drawable.ic_action_user));
		globalSettings.setIcon(getContentIcon(R.drawable.ic_action_settings));

		setupConfigureProfilePref();
		setupBrowseMapPref();
		setupManageProfilesPref();
	}

	private void setupManageProfilesPref() {
		Preference manageProfiles = findPreference("manage_profiles");
		manageProfiles.setIcon(getIcon(R.drawable.ic_action_manage_profiles));
		manageProfiles.setIntent(new Intent(getActivity(), SettingsProfileActivity.class));
	}

	private void setupBrowseMapPref() {
		Preference browseMap = findPreference("browse_map");
		browseMap.setIcon(getContentIcon(R.drawable.ic_world_globe_dark));
		Intent intent = new Intent(getActivity(), MapActivity.class);
		intent.putExtra(OPEN_CONFIG_ON_MAP, MAP_CONFIG);
		intent.putExtra(SELECTED_ITEM, getSelectedAppMode().getStringKey());
		browseMap.setIntent(intent);
	}

	private void setupConfigureProfilePref() {
		ApplicationMode selectedMode = getSelectedAppMode();

		int iconRes = selectedMode.getIconRes();
		int iconColor = selectedMode.getIconColorInfo().getColor(isNightMode());
		String title = selectedMode.toHumanString(getContext());

		String profileType;
		if (selectedMode.isCustomProfile()) {
			profileType = String.format(getString(R.string.profile_type_descr_string), Algorithms.capitalizeFirstLetterAndLowercase(selectedMode.getParent().toHumanString(getContext())));
		} else {
			profileType = getString(R.string.profile_type_base_string);
		}

		Preference configureProfile = findPreference("configure_profile");
		configureProfile.setIcon(getIcon(iconRes, iconColor));
		configureProfile.setTitle(title);
		configureProfile.setSummary(profileType);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("browse_map")) {
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			return false;
		}

		return super.onPreferenceClick(preference);
	}

	public static boolean showInstance(FragmentManager fragmentManager) {
		try {
			SettingsMainFragment settingsMainFragment = new SettingsMainFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.fragmentContainer, settingsMainFragment, SettingsMainFragment.TAG)
					.addToBackStack(SettingsMainFragment.TAG)
					.commitAllowingStateLoss();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}