# Test case for AudioSystem.setDeviceConnectionState() on Android P

https://issuetracker.google.com/issues/78456922

### What was the expected result?
That the corresponding device's connection state is changed and AUDIO_OK is returned,
or AUDIO_ERROR / ACCESS_DENIED on failure.

### What was the actual result?
Transactions is declined as unauthorized, but still returns AUDIO_OK, suggesting change in behavior is unintended.

### Expected output from 8.1 Oreo:
```
I/AudioSystemTest: Test case method called
D/PermissionCache: checking android.permission.MODIFY_AUDIO_SETTINGS for uid=10091 => granted (564 us)
W/APM_AudioPolicyManager: setDeviceConnectionState() device already connected: 8
I/AudioSystemTest: Ret value: 1 //AUDIO_ERROR since already connected. Expected result.
```

### Actual output on P:
```
I/AudioSystemTest: Test case method called
W/zygote: Accessing hidden method Landroid/media/AudioSystem;->setDeviceConnectionState(IILjava/lang/String;Ljava/lang/String;)I (light greylist, reflection) //Only light-greylisted, so should be fine on P
W/IAudioPolicyService: onTransact: transaction 1 received from PID 3735 unauthorized UID 10091 //Unauthorized, but not because of permissions
I/AudioSystemTest: Ret value: 0 //AUDIO_OK, even though it failed.
```

```IAudioPolicyService``` declines the transaction as unauthorized after successfully binding to it in ```AudioSystem::setDeviceConnectionState()```, which would return ```PERMISSION_DENIED``` ( -1 ) on failure.

This happens before ```AudioPolicyService::setDeviceConnectionState()```, where ```audioflinger\ServiceUtilities::settingsAllowed()``` is called to check if the caller has the necessary permissions ( ```android.permission.MODIFY_AUDIO_SETTINGS``` ).

The fact that the method returns ```AUDIO_OK``` even on failure, is deemed unauthorized before standard permissions are checked, and is greylisted rather than blacklisted would suggest that this is unintended behavior.

If access will no longer allowed (*sobs*) the method should be blacklisted, or at least return ```AUDIO_ERROR``` on failure.
