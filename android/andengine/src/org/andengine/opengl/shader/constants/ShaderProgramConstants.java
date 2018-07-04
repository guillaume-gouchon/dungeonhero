package org.andengine.opengl.shader.constants;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 01:03:16 - 07.08.2011
 */
public interface ShaderProgramConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	int LOCATION_INVALID = -1;

	String ATTRIBUTE_POSITION = "a_position";
	int ATTRIBUTE_POSITION_LOCATION = 0;
	String ATTRIBUTE_COLOR = "a_color";
	int ATTRIBUTE_COLOR_LOCATION = 1;
	String ATTRIBUTE_NORMAL = "a_normal";
	int ATTRIBUTE_NORMAL_LOCATION = 2;
	String ATTRIBUTE_TEXTURECOORDINATES = "a_textureCoordinates";
	int ATTRIBUTE_TEXTURECOORDINATES_LOCATION = 3;
	String ATTRIBUTE_POSITION_0 = "a_position_0";
	int ATTRIBUTE_POSITION_0_LOCATION = 4;
	String ATTRIBUTE_POSITION_1 = "a_position_1";
	int ATTRIBUTE_POSITION_1_LOCATION = 5;
	String ATTRIBUTE_POSITION_2 = "a_position_2";
	int ATTRIBUTE_POSITION_2_LOCATION = 6;

	String UNIFORM_MODELVIEWPROJECTIONMATRIX = "u_modelViewProjectionMatrix";
	String UNIFORM_MODELVIEWMATRIX = "u_modelViewMatrix";
	String UNIFORM_PROJECTIONMATRIX = "u_projectionMatrix";
	String UNIFORM_COLOR = "u_color";
	String UNIFORM_RED = "u_red";
	String UNIFORM_GREEN = "u_green";
	String UNIFORM_BLUE = "u_blue";
	String UNIFORM_ALPHA = "u_alpha";
	String UNIFORM_TEXTURE_0 = "u_texture_0";
	String UNIFORM_TEXTURE_1 = "u_texture_1";
	String UNIFORM_TEXTURE_2 = "u_texture_2";
	String UNIFORM_TEXTURE_3 = "u_texture_3";
	String UNIFORM_TEXTURE_4 = "u_texture_4";
	String UNIFORM_TEXTURE_5 = "u_texture_5";
	String UNIFORM_TEXTURE_6 = "u_texture_6";
	String UNIFORM_TEXTURE_7 = "u_texture_7";
	String UNIFORM_TEXTURE_8 = "u_texture_8";
	String UNIFORM_TEXTURE_9 = "u_texture_9";
	String UNIFORM_TEXTURESELECT_TEXTURE_0 = "u_textureselect_texture_0";
	String UNIFORM_POSITION_INTERPOLATION_MIX_0 = "u_position_interpolation_mix_0";
	String UNIFORM_POSITION_INTERPOLATION_MIX_1 = "u_position_interpolation_mix_1";

	String VARYING_TEXTURECOORDINATES = "v_textureCoordinates";
	String VARYING_COLOR = "v_color";

	// ===========================================================
	// Methods
	// ===========================================================
}
