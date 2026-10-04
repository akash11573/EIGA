package com.eiga.model;
import java.time.LocalDateTime;
public class SeatLock {
    private String lockId, showId, seatId, userId, status;
    private LocalDateTime lockedAt, expiresAt;
    
    public String getLockId() { return lockId; } public void setLockId(String lockId) { this.lockId = lockId; }
    public String getShowId() { return showId; } public void setShowId(String showId) { this.showId = showId; }
    public String getSeatId() { return seatId; } public void setSeatId(String seatId) { this.seatId = seatId; }
    public String getUserId() { return userId; } public void setUserId(String userId) { this.userId = userId; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLockedAt() { return lockedAt; } public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; } public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
