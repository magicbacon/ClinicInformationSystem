package edu.stevens.cs548.clinic.service.rest.resources;

public class Utils {
	protected static long parseIdFromLink(String link) {
		String[] parts = link.split("/");
		if (parts.length == 0) {
			return 0;
		}
		return Long.parseLong(parts[parts.length - 1]);
	}
}
