package org.ninehertzindia.clipped.fragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.CallScreenActivity;
import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.SplashActivity;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.fileutiles.FileUtils;
import org.ninehertzindia.clipped.genrics.AssignedStatus;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;
import org.ninehertzindia.clipped.util.HistoryVO;

import com.google.gson.Gson;
import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint({ "NewApi" })
public class UserDetailFragment extends Fragment implements ResponseController {
	private Activity activity;
	private View view;
	private TextView middletxt;
	private String name;
	private String image;
	private String mobile;
	private String userid;
	private ImageView imgProfile;
	private TextView txt_username;
	private TextView txtCall;
	private TextView UserPhoneno;	
	private LinearLayout AudioAssign, VideoAssign, textAssign;
	private int Audio_File_Fetch = 1, Video_File_Fetch = 2, Text_File_Fetch = 3;
	private String audio_path = "";
	private CommonMethods commonMethods;
	private DatabaseHandler db;
	private AssignedStatus assignedGenricItems;
	public DateFormat timeFormate, dateFormate;
	Date date1, date2;

	private int taskType;
	private int taskAssigne;
	private String CallTime, CallDate;

	public UserDetailFragment() {
	}

	public static UserDetailFragment newInstance() {

		UserDetailFragment fragment = new UserDetailFragment();
		return fragment;

	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.user_detail_fragment, paramViewGroup, false);

		System.out.println("hhhhhh egistrationId:" + SplashActivity.registrationId);
		setBodyUI();
		return view;
	}

	private void setBodyUI() {

		activity = getActivity();

		commonMethods = new CommonMethods(activity, this);
		db = new DatabaseHandler(activity);

		name = getArguments().getString("name");
		image = getArguments().getString("image");
		mobile = getArguments().getString("mobile");
		userid = getArguments().getString("userid");

		imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
		txt_username = (TextView) view.findViewById(R.id.txt_username);
		txtCall = (TextView) view.findViewById(R.id.txtCall);

		AudioAssign = (LinearLayout) view.findViewById(R.id.assignaudio);
		VideoAssign = (LinearLayout) view.findViewById(R.id.LLVideoAssign);
		textAssign = (LinearLayout) view.findViewById(R.id.LLTextAssign);
		

		txt_username.setText(name);
		
		
		try {
			Picasso.with(activity).load(image).fit().into(imgProfile);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ImageView imgback = (ImageView) view.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		imgback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				activity.onBackPressed();

			}
		});
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Detail");

		VideoAssign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CommonMethods.getPreferences(activity, "paymentstatus").equalsIgnoreCase("free")
						|| CommonMethods.getPreferences(activity, "paymentstatus").equalsIgnoreCase("")) {

					if (commonMethods.getConnectivityStatus()) {

						try {
							taskType = 34;
							taskAssigne = 34;
							commonMethods.CheckSettone(commonMethods.getCheckSettoneRequestParmas(
									CommonMethods.getPreferences(activity, "userid").toString(), userid));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						commonMethods.showMessageDialog(activity, "NO INTERNET CONNECTION");
					}

				} else {

					Intent target = FileUtils.createGetContentIntentVideo();
					// Create the chooser Intent
					Intent intent = Intent.createChooser(target, "ChooseVideo");
					try {
						startActivityForResult(intent, Video_File_Fetch);
					} catch (ActivityNotFoundException e) {
						// The reason for the existence of aFileChooser
					}

				}

			}
		});
		textAssign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CommonMethods.getPreferences(activity, "paymentstatus").equalsIgnoreCase("free")
						|| CommonMethods.getPreferences(activity, "paymentstatus").equalsIgnoreCase("")) {

					if (commonMethods.getConnectivityStatus()) {

						try {
							taskType = 34;
							taskAssigne = 35;
							commonMethods.CheckSettone(commonMethods.getCheckSettoneRequestParmas(
									CommonMethods.getPreferences(activity, "userid").toString(), userid));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						commonMethods.showMessageDialog(activity, "NO INTERNET CONNECTION");
					}

				} else {

					final Dialog dialogMapMain = new Dialog(activity);
					dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
					dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogMapMain.setContentView(R.layout.textassignpopup);
					dialogMapMain.getWindow().setGravity(Gravity.CENTER);

					final EditText textAssign = (EditText) dialogMapMain.findViewById(R.id.txtassign);

					final TextView okbutton = (TextView) dialogMapMain.findViewById(R.id.txt_nextt);
					final TextView cancel = (TextView) dialogMapMain.findViewById(R.id.txt_next);

					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							GlobalConfig.showToast(activity, "Cancelled");
							dialogMapMain.dismiss();

						}
					});

					okbutton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							String stext = textAssign.getText().toString();

							/*
							 * ContactVO temp = db.getContactVO(mobile);
							 * 
							 * temp.setContactDataType(Text_File_Fetch);
							 * temp.setContactDataUrl(stext);
							 * 
							 * try { System.out.println("hhhhh " +
							 * db.updateContactVO(temp));
							 * db.updateContactVO(temp); } catch (Exception e) {
							 * // TODO Auto-generated catch block
							 * e.printStackTrace(); }
							 * GlobalConfig.showToast(activity,
							 * "Successfully Text assigned.");
							 * System.out.println( "hhhhh.....after data type" +
							 * db.getContactVO(mobile).getContactDataType());
							 */

							dialogMapMain.dismiss();
							File file = null;

							if (commonMethods.getConnectivityStatus()) {

								try {
									System.out.println("hhhhhppp//" + GlobalConfig.userId + "//userid" + userid
											+ "//Textfilefile" + Text_File_Fetch);

									commonMethods.setPingtone(commonMethods.setRingtoneRequestParamas(
											CommonMethods.getPreferences(activity, "userid"), userid, file,
											"" + Text_File_Fetch, "" + stext));

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								GlobalConfig.showToast(activity, "No INTERNET CONNECTION");
							}

						}
					});

					dialogMapMain.show();

				}

			}

		});

		AudioAssign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CommonMethods.getPreferences(activity, "paymentstatus").equalsIgnoreCase("free")
						|| CommonMethods.getPreferences(activity, "paymentstatus").equalsIgnoreCase("")) {

					if (commonMethods.getConnectivityStatus()) {

						try {
							taskType = 34;
							taskAssigne = 33;
							commonMethods.CheckSettone(commonMethods.getCheckSettoneRequestParmas(
									CommonMethods.getPreferences(activity, "userid").toString(), userid));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						commonMethods.showMessageDialog(activity, "NO INTERNET CONNECTION");
					}

				} else {

					/*
					 * Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					 * i.setType("audio/*"); Intent c = Intent.createChooser(i,
					 * "Select soundfile"); startActivityForResult(c,
					 * Audio_File_Fetch);
					 */

					// Use the GET_CONTENT intent from the utility class
					Intent target = FileUtils.createGetContentIntentAudio();
					// Create the chooser Intent
					Intent intent = Intent.createChooser(target, "ChooseAudio");
					try {
						startActivityForResult(intent, Audio_File_Fetch);
					} catch (ActivityNotFoundException e) {
						// The reason for the existence of aFileChooser
					}
				}

			}
		});

		txtCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (commonMethods.getConnectivityStatus())

				{
					timeFormate = new SimpleDateFormat("dd/MM/yyyy");
					dateFormate = new SimpleDateFormat("hh:mm aa");

					date1 = new Date();
					date2 = new Date();

					CallTime = "" + timeFormate.format(date1);
					CallDate = "" + dateFormate.format(date2);

					try {
						HistoryVO historyVO = new HistoryVO(0, name, mobile, image, CallDate, CallTime,
								"outgoing call");
						db.addHistoryVO(historyVO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Call call = ClippedService.mSinchClient.getCallClient().callUser(mobile);
					String callId = call.getCallId();

					Intent callScreen = new Intent(activity, CallScreenActivity.class);
					callScreen.putExtra(ClippedService.CALL_ID, callId);
					callScreen.putExtra("image", image);
					callScreen.putExtra("localname", name);
					activity.startActivity(callScreen);
				} else {
					GlobalConfig.showToast(activity, "NO Internet Connection");
				}

				/*
				 * Call call = ((DemoActivity)
				 * activity).getSinchServiceInterface().callUser(name); String
				 * callId = call.getCallId();
				 * 
				 * Intent callScreen = new Intent(activity,
				 * CallScreenActivity.class);
				 * callScreen.putExtra(SinchService.CALL_ID, callId);
				 * callScreen.putExtra("image", image);
				 * activity.startActivity(callScreen);
				 */
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == Audio_File_Fetch) {

				final Uri uri = data.getData();
				System.out.println("hhhhhh Uri = " + uri.toString());
				try {
					// Get the file path from the URI
					audio_path = FileUtils.getPath(activity, uri);

					File f = new File(audio_path);

					String extension = MimeTypeMap.getFileExtensionFromUrl(audio_path);
					System.out.println("hhhhhkkkkkkk" + MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));

					String file_type = URLConnection.guessContentTypeFromName(f.getName());

					int FSize = (int) (f.length() / 1024);

					System.out.println("hhhhhhhhhhhh file size1" + FSize);

					FSize = FSize / 1024;
					System.out.println("hhhhhhhhhhhh file size2" + FSize);

					System.out.println("hhhhhhhhhhhhm" + file_type.substring(0, 5) + "Size " + FSize);

					System.out.println("hhhhhhhhhhhhm" + file_type.substring(0, 5));

					if (file_type.substring(0, 5).equalsIgnoreCase("audio")) {

						/*
						 * System.out.println("hhhh audio" + audio_path);
						 * 
						 * System.out.println("hhhhh.....data type" +
						 * db.getContactVO(mobile).getContactDataType());
						 * ContactVO temp = db.getContactVO(mobile);
						 * 
						 * temp.setContactDataType(Audio_File_Fetch);
						 * temp.setContactDataUrl(audio_path);
						 * 
						 * try { System.out.println("hhhhh " +
						 * db.updateContactVO(temp)); db.updateContactVO(temp);
						 * } catch (Exception e) { // TODO Auto-generated catch
						 * block e.printStackTrace(); }
						 * GlobalConfig.showToast(activity,
						 * "Successfully Audio assigned."); System.out.println(
						 * "hhhhh.....after data type" +
						 * db.getContactVO(mobile).getContactDataType());
						 */

						if (FSize <= 15) {

							if (commonMethods.getConnectivityStatus()) {

								try {
									System.out.println("hhhhhppp" + CommonMethods.getPreferences(activity, "userid")
											+ "userid" + userid + "file:-" + f + "Audiofile" + Audio_File_Fetch);

									commonMethods.setPingtone(commonMethods.setRingtoneRequestParamas(
											CommonMethods.getPreferences(activity, "userid"), userid, f,
											"" + Audio_File_Fetch, ""));

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								GlobalConfig.showToast(activity, "No INTERNET CONNECTION");
							}
						} else {

							GlobalConfig.showToast(activity, "Check File Size...");
							GlobalConfig.showToast(activity, "Size is above 15MB");

						}

					} else {
						GlobalConfig.showToast(activity, "Check File Type...");
					}

				} catch (Exception e) {
					Log.e("FileSelectorTestActivity", "File select error", e);
				}

			}
			if (requestCode == Video_File_Fetch) {
				String video_path = null;

				/*
				 * Uri selectedmp3 = data.getData(); String video_path =
				 * getpathring(selectedmp3);// getpathring(selectedmp3);
				 */
				final Uri uri = data.getData();
				System.out.println("hhhhhh Uri = " + uri.toString());
				try {
					// Get the file path from the URI
					video_path = FileUtils.getPath(activity, uri);

					System.out.println("File Selected: " + audio_path);
				} catch (Exception e) {
					Log.e("FileSelectorTestActivity", "File select error", e);
				}

				File f = new File(video_path);

				String file_type = URLConnection.guessContentTypeFromName(f.getName());

				int FSize = (int) (f.length() / 1024);

				System.out.println("hhhhhhhhhhhh file size1" + FSize);

				FSize = FSize / 1024;
				System.out.println("hhhhhhhhhhhh file size2" + FSize);

				System.out.println("hhhhhhhhhhhhm" + file_type.substring(0, 5) + "Size " + FSize);

				System.out.println("hhhhhhhhh Video File Path" + video_path);

				if (file_type.substring(0, 5).equalsIgnoreCase("video")) {

					/*
					 * System.out.println("hhhhh.....data type" +
					 * db.getContactVO(mobile).getContactDataType()); ContactVO
					 * temp = db.getContactVO(mobile);
					 * 
					 * temp.setContactDataType(Video_File_Fetch);
					 * temp.setContactDataUrl(video_path);
					 * 
					 * try { System.out.println("hhhhh " +
					 * db.updateContactVO(temp)); db.updateContactVO(temp); }
					 * catch (Exception e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); } GlobalConfig.showToast(activity,
					 * "Successfully Video assigned."); System.out.println(
					 * "hhhhh.....after data type" +
					 * db.getContactVO(mobile).getContactDataType());
					 */

					if (FSize <= 45) {

						if (commonMethods.getConnectivityStatus()) {

							try {

								System.out.println("hhhhhppp" + GlobalConfig.userId + "userid" + userid + "file:-" + f
										+ "Video" + Video_File_Fetch);

								commonMethods.setPingtone(commonMethods.setRingtoneRequestParamas(
										CommonMethods.getPreferences(activity, "userid"), userid, f,
										"" + Video_File_Fetch, ""));

							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							GlobalConfig.showToast(activity, "No INTERNET CONNECTION");
						}
					} else {

						GlobalConfig.showToast(activity, "Check File Size...");
						GlobalConfig.showToast(activity, "Size is above 45MB");

					}

				} else {
					GlobalConfig.showToast(activity, "Check File Type...");
				}

			}

		} else {
			GlobalConfig.showToast(activity, "Cancelled");
		}

	}

	public String getpathring(Uri path) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = activity.getContentResolver().query(path, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static String getPathAlg(Context context, Uri uri) throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	@SuppressLint("NewApi")
	private String getPath(Uri uri) {
		if (uri == null) {
			return null;
		}

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor;
		if (Build.VERSION.SDK_INT > 19) {
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(uri);
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
			// where id is equal to
			String sel = MediaStore.Images.Media._ID + "=?";

			cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, sel,
					new String[] { id }, null);
		} else {
			cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		}
		String path = null;

		int columnIndex = cursor.getColumnIndex(projection[0]);

		if (cursor.moveToFirst()) {
			path = cursor.getString(columnIndex);
		}
		cursor.close();
		// setImageFromIntent(filePath);
		return path;
	}

	@Override
	public void OnComplete(APIResponse apiResponse) {

		System.out.println("hhhhresponse" + apiResponse.getResponse());
		System.out.println("hhhhresponse" + apiResponse.getCode());
		if (taskType != 34) {

			if (apiResponse.getCode() == 200) {

				System.out.println("hhhhresponseCode get");
				Gson gson = new Gson();

				assignedGenricItems = gson.fromJson(apiResponse.getResponse(), AssignedStatus.class);

				if (assignedGenricItems.getStatus().equalsIgnoreCase("true")) {
					System.out.println("hhhhresponsestatus true");

					GlobalConfig.showToast(activity, "Successfully Assigned..");

				} else {
					GlobalConfig.showToast(activity, "" + assignedGenricItems.getMsg());
				}
			} else {
				GlobalConfig.showToast(activity, "Internet too slow");
			}
		} else {

			taskType = 0;
			try {
				JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
				Log.d("Response", "" + apiResponse.getResponse() + "code" + apiResponse.getCode());
				String strMsg = rootJsonObjObject.getString("msg");
				String strStatus = rootJsonObjObject.getString("status");
				if (strStatus.equalsIgnoreCase("true")) {

					if (taskAssigne == 34) {
						taskAssigne = 0;
						Intent target = FileUtils.createGetContentIntentVideo();
						// Create the chooser Intent
						Intent intent = Intent.createChooser(target, "ChooseVideo");
						try {
							startActivityForResult(intent, Video_File_Fetch);
						} catch (ActivityNotFoundException e) {
							// The reason for the existence of aFileChooser
						}

					} else if (taskAssigne == 33) {
						taskAssigne = 0;
						Intent target = FileUtils.createGetContentIntentAudio();
						// Create the chooser Intent
						Intent intent = Intent.createChooser(target, "ChooseAudio");
						try {
							startActivityForResult(intent, Audio_File_Fetch);
						} catch (ActivityNotFoundException e) {
							// The reason for the existence of aFileChooser
						}

					} else {
						taskAssigne = 0;

						final Dialog dialogMapMain = new Dialog(activity);
						dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
						dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialogMapMain.setContentView(R.layout.textassignpopup);
						dialogMapMain.getWindow().setGravity(Gravity.CENTER);

						final EditText textAssign = (EditText) dialogMapMain.findViewById(R.id.txtassign);

						final TextView okbutton = (TextView) dialogMapMain.findViewById(R.id.txt_nextt);
						final TextView cancel = (TextView) dialogMapMain.findViewById(R.id.txt_next);

						cancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								GlobalConfig.showToast(activity, "Cancelled");
								dialogMapMain.dismiss();

							}
						});

						okbutton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								String stext = textAssign.getText().toString();

								/*
								 * ContactVO temp = db.getContactVO(mobile);
								 * 
								 * temp.setContactDataType(Text_File_Fetch);
								 * temp.setContactDataUrl(stext);
								 * 
								 * try { System.out.println("hhhhh " +
								 * db.updateContactVO(temp));
								 * db.updateContactVO(temp); } catch (Exception
								 * e) { // TODO Auto-generated catch block
								 * e.printStackTrace(); }
								 * GlobalConfig.showToast(activity,
								 * "Successfully Text assigned.");
								 * System.out.println(
								 * "hhhhh.....after data type" +
								 * db.getContactVO(mobile).getContactDataType())
								 * ;
								 */

								dialogMapMain.dismiss();
								File file = null;

								if (commonMethods.getConnectivityStatus()) {

									try {
										System.out.println("hhhhhppp//" + GlobalConfig.userId + "//userid" + userid
												+ "//Textfilefile" + Text_File_Fetch);

										commonMethods.setPingtone(commonMethods.setRingtoneRequestParamas(
												CommonMethods.getPreferences(activity, "userid"), userid, file,
												"" + Text_File_Fetch, "" + stext));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else {
									GlobalConfig.showToast(activity, "No INTERNET CONNECTION");
								}

							}
						});

						dialogMapMain.show();

					}

				} else {
					GlobalConfig.showToast(activity, "You Are Not Able to Assigned Clip");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * if (apiResponse.getCode() == 200) {
			 * 
			 * System.out.println("hhhhresponseCode get"); Gson gson = new
			 * Gson();
			 * 
			 * assignedGenricItems = gson.fromJson(apiResponse.getResponse(),
			 * AssignedStatus.class);
			 * 
			 * if (assignedGenricItems.getStatus().equalsIgnoreCase("true")) {
			 * System.out.println("hhhhresponsestatus true");
			 * 
			 * GlobalConfig.showToast(activity, "Successfully Assigned..");
			 * 
			 * } else { GlobalConfig.showToast(activity, "" +
			 * assignedGenricItems.getMsg()); } } else {
			 * GlobalConfig.showToast(activity, "Internet too slow"); }
			 */

		}

	}

	// public String getPathh(Uri uri) {
	// String[] projection = { MediaStore.Images.Media.DATA };
	// Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
	// int column_index =
	// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	// cursor.moveToFirst();
	// return cursor.getString(column_index);
	// }
	//
	// @SuppressLint("NewApi")
	// private String getPath(Uri uri) {
	// if( uri == null ) {
	// return null;
	// }
	//
	// String[] projection = { MediaStore.Images.Media.DATA };
	//
	// Cursor cursor;
	// if(Build.VERSION.SDK_INT >19)
	// {
	// // Will return "image:x*"
	// String wholeID = DocumentsContract.getDocumentId(uri);
	// // Split at colon, use second item in the array
	// String id = wholeID.split(":")[1];
	// // where id is equal to
	// String sel = MediaStore.Images.Media._ID + "=?";
	//
	// cursor =
	// activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// projection, sel, new String[]{ id }, null);
	// }
	// else
	// {
	// cursor = activity.getContentResolver().query(uri, projection, null, null,
	// null);
	// }
	// String path = null;
	// try
	// {
	// int column_index = cursor
	// .getColumnIndex(MediaStore.Images.Media.DATA);
	// cursor.moveToFirst();
	// if (cursor.moveToFirst()) {
	// path = cursor.getString(column_index).toString();
	// //path = cursor.getString(columnIndex);
	// }
	//
	// cursor.close();
	// }
	// catch(NullPointerException e) {
	//
	// }
	// return path;
	// }
	/*
	 * public static String getPath(Context context,final Uri uri) {
	 * 
	 * final boolean isKitKat = Build.VERSION.SDK_INT >=
	 * Build.VERSION_CODES.KITKAT;
	 * 
	 * // DocumentProvider if (isKitKat &&
	 * DocumentsContract.isDocumentUri(context, uri)) { //
	 * ExternalStorageProvider if (isExternalStorageDocument(uri)) { final
	 * String docId = DocumentsContract.getDocumentId(uri); final String[] split
	 * = docId.split(":"); final String type = split[0];
	 * 
	 * if ("primary".equalsIgnoreCase(type)) { return
	 * Environment.getExternalStorageDirectory() + "/" + split[1]; }
	 * 
	 * // TODO handle non-primary volumes } // DownloadsProvider else if
	 * (isDownloadsDocument(uri)) {
	 * 
	 * final String id = DocumentsContract.getDocumentId(uri); final Uri
	 * contentUri = ContentUris.withAppendedId(
	 * Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
	 * 
	 * return getDataColumn(context, contentUri, null, null); } // MediaProvider
	 * else if (isMediaDocument(uri)) { final String docId =
	 * DocumentsContract.getDocumentId(uri); final String[] split =
	 * docId.split(":"); final String type = split[0];
	 * 
	 * Uri contentUri = null; if ("image".equals(type)) { contentUri =
	 * MediaStore.Images.Media.EXTERNAL_CONTENT_URI; } else if
	 * ("video".equals(type)) { contentUri =
	 * MediaStore.Video.Media.EXTERNAL_CONTENT_URI; } else if
	 * ("audio".equals(type)) { contentUri =
	 * MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; }
	 * 
	 * final String selection = "_id=?"; final String[] selectionArgs = new
	 * String[] { split[1] };
	 * 
	 * return getDataColumn(context, contentUri, selection, selectionArgs); } }
	 * // MediaStore (and general) else if
	 * ("content".equalsIgnoreCase(uri.getScheme())) { return
	 * getDataColumn(context, uri, null, null); } // File else if
	 * ("file".equalsIgnoreCase(uri.getScheme())) { return uri.getPath(); }
	 * 
	 * return null; }
	 */

}
