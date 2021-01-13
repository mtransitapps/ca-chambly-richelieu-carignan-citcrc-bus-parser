package org.mtransit.parser.ca_chambly_richelieu_carignan_citcrc_bus;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mtransit.parser.Constants.EMPTY;

// https://exo.quebec/en/about/open-data
// https://exo.quebec/xdata/citcrc/google_transit.zip
public class ChamblyRichelieuCarignanCITCRCBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-chambly-richelieu-carignan-citcrc-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new ChamblyRichelieuCarignanCITCRCBusAgencyTools().start(args);
	}

	@Nullable
	private HashSet<Integer> serviceIdInts;

	@Override
	public void start(@NotNull String[] args) {
		MTLog.log("Generating CITCRC bus data...");
		long start = System.currentTimeMillis();
		this.serviceIdInts = extractUsefulServiceIdInts(args, this, true);
		super.start(args);
		MTLog.log("Generating CITCRC bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIdInts != null && this.serviceIdInts.isEmpty();
	}

	@Override
	public boolean excludeCalendar(@NotNull GCalendar gCalendar) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarInt(gCalendar, this.serviceIdInts);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(@NotNull GCalendarDate gCalendarDates) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarDateInt(gCalendarDates, this.serviceIdInts);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		if (this.serviceIdInts != null) {
			return excludeUselessTripInt(gTrip, this.serviceIdInts);
		}
		return super.excludeTrip(gTrip);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@NotNull
	@Override
	public String getRouteLongName(@NotNull GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongNameOrDefault();
		routeLongName = CleanUtils.cleanLabelFR(routeLongName);
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String AGENCY_COLOR = "1F1F1F"; // DARK GRAY (from GTFS)

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String RSN_10 = "10";
	private static final String RSN_11 = "11";
	private static final String RSN_12 = "12";
	private static final String RSN_13 = "13";
	private static final String RSN_14 = "14";
	private static final String RSN_15 = "15";
	private static final String RSN_16 = "16";
	private static final String RSN_20 = "20";
	private static final String RSN_300 = "300";
	private static final String RSN_301 = "301";
	private static final String RSN_302 = "302";
	private static final String RSN_303 = "303";
	private static final String RSN_400 = "400";
	private static final String RSN_401 = "401";
	private static final String RSN_450 = "450";
	private static final String RSN_500 = "500";
	private static final String RSN_600 = "600";

	private static final String COLOR_74797D = "74797D";
	private static final String COLOR_ACAA00 = "ACAA00";
	private static final String COLOR_666666 = "666666";
	private static final String COLOR_EF7B0A = "EF7B0A";
	private static final String COLOR_BFD885 = "BFD885";
	private static final String COLOR_00B5E2 = "00B5E2";
	private static final String COLOR_D50080 = "D50080";
	private static final String COLOR_20A74B = "20A74B";
	private static final String COLOR_014A99 = "014A99";
	private static final String COLOR_009486 = "009486";
	private static final String COLOR_FFDD00 = "FFDD00";
	private static final String COLOR_81378E = "81378E";
	private static final String COLOR_E5003D = "E5003D";
	private static final String COLOR_FDBF4C = "FDBF4C";

	private static final String TAXIBUS = "Taxibus";

	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor())) {
			if (RSN_10.equals(gRoute.getRouteShortName())) return COLOR_FDBF4C;
			if (RSN_11.equals(gRoute.getRouteShortName())) return COLOR_E5003D;
			if (RSN_12.equals(gRoute.getRouteShortName())) return COLOR_81378E;
			if (RSN_13.equals(gRoute.getRouteShortName())) return COLOR_FFDD00;
			if (RSN_14.equals(gRoute.getRouteShortName())) return COLOR_009486;
			if (RSN_15.equals(gRoute.getRouteShortName())) return COLOR_014A99;
			if (RSN_16.equals(gRoute.getRouteShortName())) return COLOR_20A74B;
			if (RSN_20.equals(gRoute.getRouteShortName())) return COLOR_D50080;
			if (RSN_300.equals(gRoute.getRouteShortName())) return COLOR_00B5E2;
			if (RSN_301.equals(gRoute.getRouteShortName())) return COLOR_00B5E2;
			if (RSN_302.equals(gRoute.getRouteShortName())) return COLOR_00B5E2;
			if (RSN_303.equals(gRoute.getRouteShortName())) return COLOR_00B5E2;
			if (RSN_400.equals(gRoute.getRouteShortName())) return COLOR_BFD885;
			if (RSN_401.equals(gRoute.getRouteShortName())) return COLOR_BFD885;
			if (RSN_450.equals(gRoute.getRouteShortName())) return COLOR_EF7B0A;
			if (RSN_500.equals(gRoute.getRouteShortName())) return COLOR_666666;
			if (RSN_600.equals(gRoute.getRouteShortName())) return COLOR_ACAA00;
			if (gRoute.getRouteLongNameOrDefault().contains(TAXIBUS)) return COLOR_74797D;
			throw new MTLog.Fatal("Unexpected route color for %s!", gRoute);
		}
		return super.getRouteColor(gRoute);
	}

	private static final String T = "T";

	private static final long RID_STARTS_WITH_T = 20_000L;

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
		if (!Utils.isDigitsOnly(gRoute.getRouteShortName())) {
			Matcher matcher = DIGITS.matcher(gRoute.getRouteShortName());
			if (matcher.find()) {
				int digits = Integer.parseInt(matcher.group());
				if (gRoute.getRouteShortName().startsWith(T)) {
					return RID_STARTS_WITH_T + digits;
				}
			}
			throw new MTLog.Fatal("Unexpected route ID for %s!", gRoute);
		}
		return Long.parseLong(gRoute.getRouteShortName());
	}

	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		mTrip.setHeadsignString(
				cleanTripHeadsign(gTrip.getTripHeadsignOrDefault()),
				gTrip.getDirectionIdOrDefault()
		);
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		throw new MTLog.Fatal("Unexpected trips to merge %s & %s!", mTrip, mTripToMerge);
	}

	private static final Pattern DIRECTION_ = Pattern.compile("(direction )", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = DIRECTION_.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = CleanUtils.cleanStreetTypesFRCA(tripHeadsign);
		return CleanUtils.cleanLabelFR(tripHeadsign);
	}

	private static final Pattern START_WITH_FACE_A = Pattern.compile("^(face à )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);
	private static final Pattern START_WITH_FACE_AU = Pattern.compile("^(face au )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern START_WITH_FACE = Pattern.compile("^(face )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	private static final Pattern SPACE_FACE_A = Pattern.compile("( face à )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);
	private static final Pattern SPACE_WITH_FACE_AU = Pattern.compile("( face au )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern SPACE_WITH_FACE = Pattern.compile("( face )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	private static final Pattern[] START_WITH_FACES = new Pattern[]{START_WITH_FACE_A, START_WITH_FACE_AU, START_WITH_FACE};

	private static final Pattern[] SPACE_FACES = new Pattern[]{SPACE_FACE_A, SPACE_WITH_FACE_AU, SPACE_WITH_FACE};

	private static final Pattern AVENUE = Pattern.compile("( avenue)", Pattern.CASE_INSENSITIVE);
	private static final String AVENUE_REPLACEMENT = " av.";

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = AVENUE.matcher(gStopName).replaceAll(AVENUE_REPLACEMENT);
		gStopName = Utils.replaceAllNN(gStopName, START_WITH_FACES, CleanUtils.SPACE);
		gStopName = Utils.replaceAllNN(gStopName, SPACE_FACES, CleanUtils.SPACE);
		return CleanUtils.cleanLabelFR(gStopName);
	}

	private static final String ZERO = "0";

	@NotNull
	@Override
	public String getStopCode(@NotNull GStop gStop) {
		if (ZERO.equals(gStop.getStopCode())) {
			return EMPTY;
		}
		return super.getStopCode(gStop);
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	private static final String CHB = "CHB";
	private static final String LON = "LON";

	private static final String A = "A";
	private static final String B = "B";
	private static final String C = "C";
	private static final String D = "D";

	@Override
	public int getStopId(@NotNull GStop gStop) {
		String stopCode = getStopCode(gStop);
		if (stopCode.length() > 0) {
			return Integer.parseInt(stopCode); // using stop code as stop ID
		}
		//noinspection deprecation
		final String stopId1 = gStop.getStopId();
		Matcher matcher = DIGITS.matcher(stopId1);
		if (matcher.find()) {
			int digits = Integer.parseInt(matcher.group());
			int stopId;
			if (stopId1.startsWith(LON)) {
				stopId = 100000;
			} else if (stopId1.startsWith(CHB)) {
				stopId = 200000;
			} else if (stopId1.startsWith("SJR")) {
				stopId = 300000;
			} else {
				throw new MTLog.Fatal("Stop doesn't have an ID (start with)! %s", gStop);
			}
			if (stopId1.endsWith(A)) {
				stopId += 1000;
			} else if (stopId1.endsWith(B)) {
				stopId += 2000;
			} else if (stopId1.endsWith(C)) {
				stopId += 3000;
			} else if (stopId1.endsWith(D)) {
				stopId += 4000;
			} else {
				throw new MTLog.Fatal("Stop doesn't have an ID (end with)! %s", gStop);
			}
			return stopId + digits;
		} else {
			throw new MTLog.Fatal("Unexpected stop ID! %s", gStop);
		}
	}
}
