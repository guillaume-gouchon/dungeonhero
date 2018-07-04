package org.andengine.extension.tmx.util.constants;

/**
 * See: <a href="http://sourceforge.net/apps/mediawiki/tiled/index.php?title=TMX_Map_Format">TMX Map Format</a>.
 * 
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:20:22 - 20.07.2010
 */
public interface TMXConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================

	int BYTES_PER_GLOBALTILEID = 4;

	String TAG_MAP = "map";
	String TAG_MAP_ATTRIBUTE_ORIENTATION = "orientation";
	String TAG_MAP_ATTRIBUTE_ORIENTATION_VALUE_ORTHOGONAL = "orthogonal";
	String TAG_MAP_ATTRIBUTE_ORIENTATION_VALUE_ISOMETRIC = "isometric";
	String TAG_MAP_ATTRIBUTE_WIDTH = "width";
	String TAG_MAP_ATTRIBUTE_HEIGHT = "height";
	String TAG_MAP_ATTRIBUTE_TILEWIDTH = "tilewidth";
	String TAG_MAP_ATTRIBUTE_TILEHEIGHT = "tileheight";

	String TAG_TILESET = "tileset";
	String TAG_TILESET_ATTRIBUTE_FIRSTGID = "firstgid";
	String TAG_TILESET_ATTRIBUTE_SOURCE = "source";
	String TAG_TILESET_ATTRIBUTE_NAME = "name";
	String TAG_TILESET_ATTRIBUTE_TILEWIDTH = "tilewidth";
	String TAG_TILESET_ATTRIBUTE_TILEHEIGHT = "tileheight";
	String TAG_TILESET_ATTRIBUTE_SPACING = "spacing";
	String TAG_TILESET_ATTRIBUTE_MARGIN = "margin";

	String TAG_IMAGE = "image";
	String TAG_IMAGE_ATTRIBUTE_SOURCE = "source";
	String TAG_IMAGE_ATTRIBUTE_TRANS = "trans";

	String TAG_TILE = "tile";
	String TAG_TILE_ATTRIBUTE_ID = "id";
	String TAG_TILE_ATTRIBUTE_GID = "gid";

	String TAG_PROPERTIES = "properties";

	String TAG_PROPERTY = "property";
	String TAG_PROPERTY_ATTRIBUTE_NAME = "name";
	String TAG_PROPERTY_ATTRIBUTE_VALUE = "value";

	String TAG_LAYER = "layer";
	String TAG_LAYER_ATTRIBUTE_NAME = "name";
	String TAG_LAYER_ATTRIBUTE_WIDTH = "width";
	String TAG_LAYER_ATTRIBUTE_HEIGHT = "height";
	String TAG_LAYER_ATTRIBUTE_VISIBLE = "visible";
	int TAG_LAYER_ATTRIBUTE_VISIBLE_VALUE_DEFAULT = 1;
	String TAG_LAYER_ATTRIBUTE_OPACITY = "opacity";
	float TAG_LAYER_ATTRIBUTE_OPACITY_VALUE_DEFAULT = 1.0f;

	String TAG_DATA = "data";
	String TAG_DATA_ATTRIBUTE_ENCODING = "encoding";
	String TAG_DATA_ATTRIBUTE_ENCODING_VALUE_BASE64 = "base64";
	String TAG_DATA_ATTRIBUTE_COMPRESSION = "compression";
	String TAG_DATA_ATTRIBUTE_COMPRESSION_VALUE_GZIP = "gzip";
	String TAG_DATA_ATTRIBUTE_COMPRESSION_VALUE_ZLIB = "zlib";


	String TAG_OBJECTGROUP = "objectgroup";
	String TAG_OBJECTGROUP_ATTRIBUTE_NAME = "name";
	String TAG_OBJECTGROUP_ATTRIBUTE_WIDTH = "width";
	String TAG_OBJECTGROUP_ATTRIBUTE_HEIGHT = "height";

	String TAG_OBJECT = "object";
	String TAG_OBJECT_ATTRIBUTE_NAME = "name";
	String TAG_OBJECT_ATTRIBUTE_TYPE = "type";
	String TAG_OBJECT_ATTRIBUTE_X = "x";
	String TAG_OBJECT_ATTRIBUTE_Y = "y";
	String TAG_OBJECT_ATTRIBUTE_WIDTH = "width";
	String TAG_OBJECT_ATTRIBUTE_HEIGHT = "height";

	// ===========================================================
	// Methods
	// ===========================================================
}
