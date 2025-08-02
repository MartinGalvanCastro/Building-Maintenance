# TechnicianMaintenanceRequestsApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**changeStatus**](#changestatus) | **PATCH** /technicians/{technicianId}/requests/{requestId}/status | Change the status of a maintenance request|
|[**list1**](#list1) | **GET** /technicians/{technicianId}/requests | List all maintenance requests assigned to a technician|

# **changeStatus**
> MaintenanceRequestDto changeStatus(changeStatusDto)

Allows a technician to change the status of an assigned maintenance request.

### Example

```typescript
import {
    TechnicianMaintenanceRequestsApi,
    Configuration,
    ChangeStatusDto
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianMaintenanceRequestsApi(configuration);

let technicianId: string; //UUID of the technician (default to undefined)
let requestId: string; //UUID of the maintenance request (default to undefined)
let changeStatusDto: ChangeStatusDto; //

const { status, data } = await apiInstance.changeStatus(
    technicianId,
    requestId,
    changeStatusDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **changeStatusDto** | **ChangeStatusDto**|  | |
| **technicianId** | [**string**] | UUID of the technician | defaults to undefined|
| **requestId** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Status changed successfully |  -  |
|**400** | Invalid input data |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **list1**
> Array<MaintenanceRequestDto> list1()

Returns a list of all maintenance requests assigned to the specified technician.

### Example

```typescript
import {
    TechnicianMaintenanceRequestsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianMaintenanceRequestsApi(configuration);

let technicianId: string; //UUID of the technician (default to undefined)

const { status, data } = await apiInstance.list1(
    technicianId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **technicianId** | [**string**] | UUID of the technician | defaults to undefined|


### Return type

**Array<MaintenanceRequestDto>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of maintenance requests |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

