package org.mtransit.parser.ca_chambly_richelieu_carignan_citcrc_bus;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MSpec;
import org.mtransit.parser.mt.data.MTrip;

// https://www.amt.qc.ca/en/about/open-data
// http://www.amt.qc.ca/xdata/citcrc/google_transit.zip
public class ChamblyRichelieuCarignanCITCRCBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-chambly-richelieu-carignan-citcrc-bus-android/res/raw/";
			args[2] = ""; // files-prefix
			// args[3] = "false"; // not-V1
		}
		new ChamblyRichelieuCarignanCITCRCBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;
	@Override
	public void start(String[] args) {
		System.out.printf("Generating CITCRC bus data...\n");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("Generating CITCRC bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.route_long_name;
		routeLongName = MSpec.SAINT.matcher(routeLongName).replaceAll(MSpec.SAINT_REPLACEMENT);
		return MSpec.cleanLabel(routeLongName);
	}

	private static final String AGENCY_COLOR = "D80C4A";

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

	@Override
	public String getRouteColor(GRoute gRoute) {
		if (RSN_10.equals(gRoute.route_short_name)) return COLOR_FDBF4C;
		if (RSN_11.equals(gRoute.route_short_name)) return COLOR_E5003D;
		if (RSN_12.equals(gRoute.route_short_name)) return COLOR_81378E;
		if (RSN_13.equals(gRoute.route_short_name)) return COLOR_FFDD00;
		if (RSN_14.equals(gRoute.route_short_name)) return COLOR_009486;
		if (RSN_15.equals(gRoute.route_short_name)) return COLOR_014A99;
		if (RSN_16.equals(gRoute.route_short_name)) return COLOR_20A74B;
		if (RSN_20.equals(gRoute.route_short_name)) return COLOR_D50080;
		if (RSN_300.equals(gRoute.route_short_name)) return COLOR_00B5E2;
		if (RSN_301.equals(gRoute.route_short_name)) return COLOR_00B5E2;
		if (RSN_302.equals(gRoute.route_short_name)) return COLOR_00B5E2;
		if (RSN_303.equals(gRoute.route_short_name)) return COLOR_00B5E2;
		if (RSN_400.equals(gRoute.route_short_name)) return COLOR_BFD885;
		if (RSN_401.equals(gRoute.route_short_name)) return COLOR_BFD885;
		if (RSN_450.equals(gRoute.route_short_name)) return COLOR_EF7B0A;
		if (RSN_500.equals(gRoute.route_short_name)) return COLOR_666666;
		if (RSN_600.equals(gRoute.route_short_name)) return COLOR_ACAA00;
		if (gRoute.route_long_name.contains(TAXIBUS)) return COLOR_74797D;
		System.out.println("Unexpected route color " + gRoute);
		System.exit(-1);
		return null;
	}

	@Override
	public long getRouteId(GRoute gRoute) {
		if (!Utils.isDigitsOnly(gRoute.route_id)) {
			return Long.parseLong(gRoute.route_short_name);
		}
		return super.getRouteId(gRoute);
	}

	private static final String AM = "AM";
	private static final String PM = "PM";

	@Override
	public void setTripHeadsign(MRoute route, MTrip mTrip, GTrip gTrip) {
		String stationName = cleanTripHeadsign(gTrip.trip_headsign);
		int directionId = gTrip.direction_id;
		if (mTrip.getRouteId() == 14l) {
			if (directionId == 0) {
				stationName = AM;
			} else if (directionId == 1) {
				stationName = PM;
			}
		} else if (mTrip.getRouteId() == 15l) {
			if (directionId == 0) {
				stationName = AM;
			} else if (directionId == 1) {
				stationName = PM;
			}
		}
		mTrip.setHeadsignString(stationName, directionId);
	}

	private static final Pattern DIRECTION = Pattern.compile("(direction )", Pattern.CASE_INSENSITIVE);
	private static final String DIRECTION_REPLACEMENT = "";

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = DIRECTION.matcher(tripHeadsign).replaceAll(DIRECTION_REPLACEMENT);
		return MSpec.cleanLabelFR(tripHeadsign);
	}

	private static final Pattern START_WITH_FACE_A = Pattern.compile("^(face à )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern START_WITH_FACE_AU = Pattern.compile("^(face au )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern START_WITH_FACE = Pattern.compile("^(face )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	private static final Pattern SPACE_FACE_A = Pattern.compile("( face à )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern SPACE_WITH_FACE_AU = Pattern.compile("( face au )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern SPACE_WITH_FACE = Pattern.compile("( face )", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	private static final Pattern[] START_WITH_FACES = new Pattern[] { START_WITH_FACE_A, START_WITH_FACE_AU, START_WITH_FACE };

	private static final Pattern[] SPACE_FACES = new Pattern[] { SPACE_FACE_A, SPACE_WITH_FACE_AU, SPACE_WITH_FACE };

	private static final Pattern AVENUE = Pattern.compile("( avenue)", Pattern.CASE_INSENSITIVE);
	private static final String AVENUE_REPLACEMENT = " av.";

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = AVENUE.matcher(gStopName).replaceAll(AVENUE_REPLACEMENT);
		gStopName = Utils.replaceAll(gStopName, START_WITH_FACES, MSpec.SPACE);
		gStopName = Utils.replaceAll(gStopName, SPACE_FACES, MSpec.SPACE);
		return super.cleanStopNameFR(gStopName);
	}

	@Override
	public String getStopCode(GStop gStop) {
		if ("0".equals(gStop.stop_code)) {
			return null;
		}
		return super.getStopCode(gStop);
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public int getStopId(GStop gStop) {
		String stopCode = getStopCode(gStop);
		if (stopCode != null && stopCode.length() > 0) {
			return Integer.valueOf(stopCode); // using stop code as stop ID
		}
		// generating integer stop ID
		Matcher matcher = DIGITS.matcher(gStop.stop_id);
		matcher.find();
		int digits = Integer.parseInt(matcher.group());
		int stopId;
		if (gStop.stop_id.startsWith("LON")) {
			stopId = 100000;
		} else if (gStop.stop_id.startsWith("CHB")) {
			stopId = 200000;
		} else {
			System.out.println("Stop doesn't have an ID (start with)! " + gStop);
			System.exit(-1);
			stopId = -1;
		}
		if (gStop.stop_id.endsWith("A")) {
			stopId += 1000;
		} else if (gStop.stop_id.endsWith("B")) {
			stopId += 2000;
		} else if (gStop.stop_id.endsWith("C")) {
			stopId += 3000;
		} else if (gStop.stop_id.endsWith("D")) {
			stopId += 4000;
		} else {
			System.out.println("Stop doesn't have an ID (end with)! " + gStop);
			System.exit(-1);
		}
		return stopId + digits;
	}
}
