# ResidentManagementApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**create1**](#create1) | **POST** /residents | Create a new resident|
|[**delete1**](#delete1) | **DELETE** /residents/{id} | Delete a resident|
|[**getOne1**](#getone1) | **GET** /residents/{id} | Get a resident by ID|
|[**listAll1**](#listall1) | **GET** /residents | List all residents|
|[**update1**](#update1) | **PUT** /residents/{id} | Update a resident|

# **create1**
> ResidentDto create1(residentCreateCommandDto)

Creates a new resident with the provided details.

### Example

```typescript
import {
    ResidentManagementApi,
    Configuration,
    ResidentCreateCommandDto
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentManagementApi(configuration);

let residentCreateCommandDto: ResidentCreateCommandDto; //Resident creation data

const { status, data } = await apiInstance.create1(
    residentCreateCommandDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentCreateCommandDto** | **ResidentCreateCommandDto**| Resident creation data | |


### Return type

**ResidentDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Resident created |  -  |
|**400** | Invalid input data |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete1**
> DeleteResponseDto delete1()

Deletes a resident by their ID.

### Example

```typescript
import {
    ResidentManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentManagementApi(configuration);

let id: string; //UUID of the resident (default to undefined)

const { status, data } = await apiInstance.delete1(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the resident | defaults to undefined|


### Return type

**DeleteResponseDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Resident deleted |  -  |
|**404** | Resident not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getOne1**
> ResidentDto getOne1()

Returns a resident by their unique identifier.

### Example

```typescript
import {
    ResidentManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentManagementApi(configuration);

let id: string; //UUID of the resident (default to undefined)

const { status, data } = await apiInstance.getOne1(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the resident | defaults to undefined|


### Return type

**ResidentDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Resident found |  -  |
|**404** | Resident not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listAll1**
> Array<ResidentDto> listAll1()

Returns a list of all residents.

### Example

```typescript
import {
    ResidentManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentManagementApi(configuration);

const { status, data } = await apiInstance.listAll1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ResidentDto>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of residents |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update1**
> ResidentDto update1(residentUpdateCommandDto)

Updates an existing resident by their ID.

### Example

```typescript
import {
    ResidentManagementApi,
    Configuration,
    ResidentUpdateCommandDto
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentManagementApi(configuration);

let id: string; //UUID of the resident (default to undefined)
let residentUpdateCommandDto: ResidentUpdateCommandDto; //Resident update data

const { status, data } = await apiInstance.update1(
    id,
    residentUpdateCommandDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentUpdateCommandDto** | **ResidentUpdateCommandDto**| Resident update data | |
| **id** | [**string**] | UUID of the resident | defaults to undefined|


### Return type

**ResidentDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Resident updated |  -  |
|**400** | Invalid input data |  -  |
|**404** | Resident not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

