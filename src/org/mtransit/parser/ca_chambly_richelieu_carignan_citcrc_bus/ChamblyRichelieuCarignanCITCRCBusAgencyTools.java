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

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String AGENCY_COLOR = "D80C4A";

	@Override
	public String getRouteColor(GRoute gRoute) {
		if ("11".equals(gRoute.route_short_name)) return "E5003D";
		if ("12".equals(gRoute.route_short_name)) return "81378E";
		if ("13".equals(gRoute.route_short_name)) return "FFDD00";
		if ("14".equals(gRoute.route_short_name)) return "009486";
		if ("15".equals(gRoute.route_short_name)) return "014A99";
		if ("16".equals(gRoute.route_short_name)) return "20A74B";
		if ("20".equals(gRoute.route_short_name)) return "D50080";
		if ("300".equals(gRoute.route_short_name)) return "00B5E2";
		if ("301".equals(gRoute.route_short_name)) return "00B5E2";
		if ("302".equals(gRoute.route_short_name)) return "00B5E2";
		if ("303".equals(gRoute.route_short_name)) return "00B5E2";
		if ("400".equals(gRoute.route_short_name)) return "BFD885";
		if ("401".equals(gRoute.route_short_name)) return "BFD885";
		if ("450".equals(gRoute.route_short_name)) return "EF7B0A";
		if ("500".equals(gRoute.route_short_name)) return "666666";
		if ("600".equals(gRoute.route_short_name)) return "ACAA00";
		if (gRoute.route_long_name.contains("Taxibus")) return "74797D";
		return super.getRouteColor(gRoute);
	}


	@Override
	public void setTripHeadsign(MRoute route, MTrip mTrip, GTrip gTrip) {
		String stationName = cleanTripHeadsign(gTrip.trip_headsign);
		int directionId = gTrip.direction_id;
		if (mTrip.getRouteId() == 14l) {
			if (directionId == 0) {
				stationName = "Richelieu-Chambly 0";
			} else if (directionId == 1) {
				stationName = "Richelieu-Chambly 1";
			}
		} else if (mTrip.getRouteId() == 15l) {
			if (directionId == 0) {
				stationName = "Marieville-Chambly 0";
			} else if (directionId == 1) {
				stationName = "Marieville-Chambly 1";
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
		} else {
			System.out.println("Stop doesn't have an ID (start with)! " + gStop);
			System.exit(-1);
			stopId = -1;
		}
		if (gStop.stop_id.endsWith("A")) {
			stopId += 1000;
		} else {
			System.out.println("Stop doesn't have an ID (end with)! " + gStop);
			System.exit(-1);
		}
		return stopId + digits;
	}

}
