package com.tt9smart.db.customWords;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.tt9smart.R;
import com.tt9smart.db.sqlite.ReadOps;
import com.tt9smart.db.sqlite.WordDbOpener;

public class CustomWordsExporter extends AbstractExporter {
	private static CustomWordsExporter customWordsExporterSelf;

	private static final String BASE_FILE_NAME = "tt9-added-words-export-";

	public static CustomWordsExporter getInstance() {
		if (customWordsExporterSelf == null) {
			customWordsExporterSelf = new CustomWordsExporter();
		}

		return customWordsExporterSelf;
	}

	@Override
	protected void runSync(Activity activity) {
		try {
			sendStart(activity.getString(R.string.dictionary_export_generating_csv));
			write(activity);
			sendSuccess();
		} catch (Exception e) {
			sendFailure();
		}
	}

	@Override
	@NonNull
	protected String generateFileName() {
		return BASE_FILE_NAME + "-" + System.currentTimeMillis() + FILE_EXTENSION;
	}

	@NonNull
	@Override
	protected byte[] getFileContents(Activity activity) throws Exception {
		SQLiteDatabase db = WordDbOpener.getInstance(activity).getDb();
		if (db == null) {
			throw new Exception("Could not open database");
		}

		return new ReadOps().getWords(db, null, true).getBytes();
	}
}
