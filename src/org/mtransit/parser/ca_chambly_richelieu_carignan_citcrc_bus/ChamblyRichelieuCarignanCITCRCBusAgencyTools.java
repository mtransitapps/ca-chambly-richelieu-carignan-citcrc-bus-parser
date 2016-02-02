package org.mtransit.parser.ca_chambly_richelieu_carignan_citcrc_bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.Utils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MDirectionType;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTripStop;
import org.mtransit.parser.CleanUtils;
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
		System.out.printf("\nGenerating CITCRC bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("\nGenerating CITCRC bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
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

	private static final Pattern CLEAN_CHAMBLY_LONGIEUIL = Pattern.compile("(chambly[\\s]*-[\\s]*longueuil)", Pattern.CASE_INSENSITIVE);
	private static final String CLEAN_CHAMBLY_LONGIEUIL_REPLACEMENT = "Chambly - longueuil";

	private static final Pattern CLEAN_P1 = Pattern.compile("[\\s]*\\([\\s]*");
	private static final String CLEAN_P1_REPLACEMENT = " (";
	private static final Pattern CLEAN_P2 = Pattern.compile("[\\s]*\\)[\\s]*");
	private static final String CLEAN_P2_REPLACEMENT = ") ";

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongName();
		routeLongName = CleanUtils.SAINT.matcher(routeLongName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		routeLongName = CLEAN_CHAMBLY_LONGIEUIL.matcher(routeLongName).replaceAll(CLEAN_CHAMBLY_LONGIEUIL_REPLACEMENT);
		routeLongName = CLEAN_P1.matcher(routeLongName).replaceAll(CLEAN_P1_REPLACEMENT);
		routeLongName = CLEAN_P2.matcher(routeLongName).replaceAll(CLEAN_P2_REPLACEMENT);
		return CleanUtils.cleanLabel(routeLongName);
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
		if (gRoute.getRouteLongName().contains(TAXIBUS)) return COLOR_74797D;
		System.out.println("Unexpected route color " + gRoute);
		System.exit(-1);
		return null;
	}

	@Override
	public long getRouteId(GRoute gRoute) {
		if (!Utils.isDigitsOnly(gRoute.getRouteId())) {
			return Long.parseLong(gRoute.getRouteShortName());
		}
		return super.getRouteId(gRoute);
	}

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		ALL_ROUTE_TRIPS2 = map2;
	}


	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()));
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		String tripHeadsign = cleanTripHeadsign(gTrip.getTripHeadsign());
		if (mTrip.getRouteId() == 14l) {
			if (gTrip.getDirectionId() == 1) {
				if ("Richelieu-Chambly".equalsIgnoreCase(tripHeadsign)) {
					tripHeadsign = "PM";
				}
			}
		}
		mTrip.setHeadsignString(tripHeadsign, gTrip.getDirectionId());
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		if (mTrip.getRouteId() == 3l) {
			if (mTrip.getHeadsignId() == 0) {
				mTrip.setHeadsignString("Carignan", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 5l) {
			if (mTrip.getHeadsignId() == 1) {
				mTrip.setHeadsignString("Route 112", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 14l) {
			if (mTrip.getHeadsignId() == 0) {
				mTrip.setHeadsignString("AM", mTrip.getHeadsignId());
				return true;
			} else if (mTrip.getHeadsignId() == 1) {
				mTrip.setHeadsignString("PM", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 15l) {
			if (mTrip.getHeadsignId() == 0) {
				mTrip.setHeadsignString("AM", mTrip.getHeadsignId());
				return true;
			} else if (mTrip.getHeadsignId() == 1) {
				mTrip.setHeadsignString("PM", mTrip.getHeadsignId());
				return true;
			}
		}
		System.out.printf("\nUnexpected trips to merge %s & %s!\n", mTrip, mTripToMerge);
		System.exit(-1);
		return false;
	}

	private static final Pattern DIRECTION = Pattern.compile("(direction )", Pattern.CASE_INSENSITIVE);
	private static final String DIRECTION_REPLACEMENT = "";

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = DIRECTION.matcher(tripHeadsign).replaceAll(DIRECTION_REPLACEMENT);
		return CleanUtils.cleanLabelFR(tripHeadsign);
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
		gStopName = Utils.replaceAll(gStopName, START_WITH_FACES, CleanUtils.SPACE);
		gStopName = Utils.replaceAll(gStopName, SPACE_FACES, CleanUtils.SPACE);
		return CleanUtils.cleanLabelFR(gStopName);
	}

	private static final String ZERO = "0";

	@Override
	public String getStopCode(GStop gStop) {
		if (ZERO.equals(gStop.getStopCode())) {
			return null;
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
	public int getStopId(GStop gStop) {
		String stopCode = getStopCode(gStop);
		if (stopCode != null && stopCode.length() > 0) {
			return Integer.valueOf(stopCode); // using stop code as stop ID
		}
		// generating integer stop ID
		Matcher matcher = DIGITS.matcher(gStop.getStopId());
		matcher.find();
		int digits = Integer.parseInt(matcher.group());
		int stopId;
		if (gStop.getStopId().startsWith(LON)) {
			stopId = 100000;
		} else if (gStop.getStopId().startsWith(CHB)) {
			stopId = 200000;
		} else if (gStop.getStopId().startsWith("SJR")) {
			stopId = 300000;
		} else {
			System.out.printf("\nStop doesn't have an ID (start with)! %s\n", gStop);
			System.exit(-1);
			stopId = -1;
		}
		if (gStop.getStopId().endsWith(A)) {
			stopId += 1000;
		} else if (gStop.getStopId().endsWith(B)) {
			stopId += 2000;
		} else if (gStop.getStopId().endsWith(C)) {
			stopId += 3000;
		} else if (gStop.getStopId().endsWith(D)) {
			stopId += 4000;
		} else {
			System.out.printf("\nStop doesn't have an ID (end with)! %s\n", gStop);
			System.exit(-1);
		}
		return stopId + digits;
	}
}
