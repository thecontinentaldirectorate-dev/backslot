package com.backslot;

/**
 * Whether the server on the other end of this connection is running the mod.
 *
 * <p>It announces itself by force-syncing the back item once as the player joins. On a
 * server without the mod that never arrives, and slot 46 doesn't exist there either — the
 * click handler rejects the index and the client desyncs — so the slot is hidden instead.
 *
 * <p>Only ever false on a client. Server-side code must not consult it, since in single
 * player both sides share this class.
 */
public final class BackSlotSync {
	private static volatile boolean serverHasMod = true;
	private static volatile int dataId = -1;

	public static boolean serverHasMod() {
		return serverHasMod;
	}

	public static void setServerHasMod(boolean value) {
		serverHasMod = value;
	}

	/** The synched data id is assigned at runtime, so the client has to be told what to watch for. */
	public static void rememberDataId(int id) {
		dataId = id;
	}

	public static int dataId() {
		return dataId;
	}

	private BackSlotSync() {
	}
}
