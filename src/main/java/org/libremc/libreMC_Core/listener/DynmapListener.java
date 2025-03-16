package org.libremc.libreMC_Core.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.libremc.libreMC_Core.Core;

import java.io.InputStream;

public class DynmapListener extends DynmapCommonAPIListener {

    public static MarkerAPI api = null;

    @Override
    public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {

        /* First time setup */
        MarkerAPI marker_api = dynmapCommonAPI.getMarkerAPI();
        api = marker_api;
        MarkerSet set = marker_api.getMarkerSet("markers");

        String htmlLabel = "<div>End Portal</div>";
        MarkerIcon icon = marker_api.getMarkerIcon("end_portal_icon");

        if(icon == null){
            InputStream stream = Core.getInstance().getResource("endportal_icon.png");

            if(stream == null){
                Bukkit.getLogger().warning("End portal icon not found!");
                return;
            }
            icon = marker_api.createMarkerIcon("end_portal_icon", "End Portal Icon", stream);
        }

        Marker marker = set.findMarker("end_portal_marker");
        if(marker == null) {
            marker = set.createMarker(
                    /* Marker ID */                  "end_portal_marker",
                    /* Marker label */               htmlLabel,
                    /* Process label as HTML */      true,
                    /* World to display marker in */ "world",
                    /* X coordinate */               0,
                    /* Y coordinate */               0,
                    /* Z coordinate */               0,
                    /* Related MarkerIcon object */  icon,
                    /* Marker is persistent */       true
            );

            marker.setMarkerIcon(icon);
        }
    }

    public static void moveEndPortalMarker(Location loc){
        if(api == null){
            return;
        }

        MarkerSet set = api.getMarkerSet("markers");
        Marker marker = set.findMarker("end_portal_marker");

        if(marker == null){
            return;
        }

        marker.setLocation("world", loc.getX(), loc.getY(), loc.getZ());
    }
}
